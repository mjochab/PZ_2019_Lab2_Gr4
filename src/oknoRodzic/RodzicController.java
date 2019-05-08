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
import mapping.*;
import utilities.*;
import static utilities.HibernateUtil.uzyskajPesel;

public class RodzicController implements Initializable {

    //private final long PESEL = 32222222220L;
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
    private Label userid;

    private long pesel;
    private String username;
    public String[] nazwyKolumn;
    public ObservableList<TableColumn> kolumna;
    public Uczen uczen;
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
            wpisywanieOcen();

        });

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
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

    }

    @FXML
    public void zmianaNazwKolumn() {
        nazwyKolumn = HibernateUtil.pobieranieNazwPrzedmiotow();
        kolumna = tabelaOcen.getColumns();

        if (nazwyKolumn.length != 0) {
            int i = 0;

            for (TableColumn kol : kolumna) {
                kol.setText(nazwyKolumn[i]);
                i++;
            }
        } else {
        }
    }

    public List<String> zwrocOcenyDlaPrzedmiotu(Set oceny, String nazwaKolumny) {
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

    public void wstawianieOcenDoKolumn(TableColumn<Integer, String> kol, List<String> listaOcen) {
        kol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            if (rowIndex >= listaOcen.size()) {
                return null;
            } else {
                return new ReadOnlyStringWrapper(listaOcen.get(rowIndex));
            }
        });
    }

    public void wpisywanieOcen() {
        Rodzic rodzic = HibernateUtil.zwrocRodzica(pesel);
        uczen = rodzic.getUczen();
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

    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
        this.username = username;
        this.pesel = pesel;
    }

    public void wstawUseraDoZalogowanoJako(String username) {
        userid.setText(username);
    }

    private Long getPesel() {
        String login = userid.getText();
        return uzyskajPesel(login);
    }
}
