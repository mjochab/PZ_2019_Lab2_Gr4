/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoRodzic;

import oknoUczen.*;
import utilities.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import mapping.*;
import utilities.*;
import static utilities.HibernateUtil.uzyskajPesel;

public class RodzicController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button ocenybtn;
    @FXML
    private Button nieobecnoscibtn;
    @FXML
    private Button uwagibtn;
    @FXML
    private Button wylogujbtn;
    @FXML
    private TableView<Uczen> dzieciTB;

    public void setDzieciTB(TableView<Uczen> dzieciTB) {
        this.dzieciTB = dzieciTB;
    }
    @FXML
    private TableColumn<Uczen, String> imie;
    @FXML
    private TableColumn<Uczen, String> nazwisko;

    @FXML
    private TableView tabelaOcen;
    @FXML
    private TableColumn<Integer, Number> kolumna1;
    @FXML
    private TableColumn<Integer, Number> kolumna2;
    @FXML
    private TableColumn<Integer, Number> kolumna3;
    @FXML
    private TableColumn<Integer, Number> kolumna4;
    @FXML
    private TableColumn<Integer, Number> kolumna5;
    @FXML
    private TableColumn<Integer, Number> kolumna6;
    @FXML
    private TableColumn<Integer, Number> kolumna7;
    @FXML
    private Label userLogin;

    private String username = "uzytkownik";
    private Long pesel;
    public String[] nazwyKolumn;
    public ObservableList<TableColumn> kolumna;
    public Uczen uczen;
    public Rodzic rodzic;

    public void setRodzic(Rodzic rodzic) {
        this.rodzic = rodzic;
    }
    //public 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tabelaOcen.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> {
            Utils.customResize(tabelaOcen);
            wstawUseraDoZalogowanoJako(username);
            zmianaNazwKolumn();
            pesel = getPesel();
            wstawianieDzieci();
            if (dzieciTB.getItems().isEmpty()) {
            } else {
                dzieciTB.addEventHandler(MouseEvent.MOUSE_CLICKED, zwrocEventHandleraDlaDzieci(dzieciTB));
            }
        });

    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    //ładujemy defaultowe okno z ocenami ucznia
    @FXML
    private void LoadOceny(ActionEvent event) throws IOException {
        tabelaOcen.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaOcen));
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Rodzic.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(RodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RodzicController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);
        controller.setRodzic(rodzic);
    }

    @FXML
    private void LoadNieobecnosci(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("RodzicNieobecnosci.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(RodzicNieobecnosciController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RodzicNieobecnosciController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);
        controller.setRodzic(rodzic);
    }

    @FXML
    private void LoadUwagi(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("RodzicPlan.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(RodzicPlanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RodzicPlanController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);
        controller.setRodzic(rodzic);

    }

    @FXML
    private void zmianaNazwKolumn() {
        nazwyKolumn = HibernateUtil.pobieranieNazwPrzedmiotow();
        kolumna = tabelaOcen.getColumns();
        tabelaOcen.getColumns().clear();

        for (int i = 0; i < nazwyKolumn.length; i++) {
            TableColumn<Integer, Number> przedmiot = new TableColumn<Integer, Number>();
            tabelaOcen.getColumns().add(przedmiot);
        }
        if (nazwyKolumn.length != 0) {
            int i = 0;

            for (TableColumn kol : kolumna) {
                kol.setText(nazwyKolumn[i]);
                i++;
            }
        } else {
        }
    }

    private List<String> zwrocOcenyDlaPrzedmiotu(Set oceny, String nazwaKolumny) {
        List<String> lista = new ArrayList<>();
        Iterator<Ocena> it = oceny.iterator();

        while (it.hasNext()) {
            Ocena ocena = it.next();
            if (ocena.getPrzedmiot().getNazwaPrzedmiotu().equals(nazwaKolumny)) {
                lista.add(ocena.getStopien().toString() + " - " + ocena.getRodzajOceny().getRodzajOceny());
            } else {

            }
        }
        return lista;
    }

    private void wstawianieOcenDoKolumn(TableColumn<Integer, String> kol, List<String> listaOcen) {
        kol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            if (rowIndex >= listaOcen.size()) {
                return null;
            } else {
                return new ReadOnlyStringWrapper(listaOcen.get(rowIndex));
            }
        });
    }

    private void wpisywanieOcen(Uczen uczen) {
        tabelaOcen.getItems().clear();
        Set oceny = uczen.getOcenas();
        for (int i = 0; i < oceny.size(); i++) {
            tabelaOcen.getItems().add(i);
        }
        for (TableColumn<Integer, String> kol : kolumna) {

            List<String> listaOcen = zwrocOcenyDlaPrzedmiotu(oceny, kol.getText());
            if (listaOcen.isEmpty()) {
            } else {
                wstawianieOcenDoKolumn(kol, listaOcen);
            }
        }
    }

    private EventHandler zwrocEventHandleraDlaDzieci(TableView<Uczen> table) {

        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                Uczen selectedItem = table.getSelectionModel().getSelectedItem();
                wpisywanieOcen(selectedItem);
            }
        };
        return eventHandler;
    }

    private void wstawianieDzieci() {
        rodzic = HibernateUtil.zwrocRodzica(pesel);
        Set dzieci = rodzic.getUczens();
        if (dzieci.isEmpty()) {
        } else {
            imie.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

            ObservableList<Uczen> dane = FXCollections.observableArrayList(dzieci);
            dzieciTB.setItems(dane);
        }
    }

    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
        this.username = username;
        this.pesel = pesel;
    }

    public void wstawUseraDoZalogowanoJako(String username) {
        userLogin.setText(username);
    }

    private Long getPesel() {
        String login = userLogin.getText();
        return uzyskajPesel(login);
    }
}
