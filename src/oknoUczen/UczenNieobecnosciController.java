/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoUczen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mapping.Obecnosc;
import mapping.Ocena;
import mapping.Przedmiot;
import mapping.Uczen;
import mapping.Zajecia;
import utilities.HibernateUtil;
import static utilities.HibernateUtil.uzyskajPesel;
import utilities.Utils;

public class UczenNieobecnosciController implements Initializable {

    @FXML
    private Button ocenybtn;
    @FXML
    private Button nieobecnoscibtn;
    @FXML
    private Button uwagibtn;
    @FXML
    private Button wylogujbtn;
    @FXML
    private Text increment;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView tabelaNieob;
    @FXML
    private TableColumn<Obecnosc, String> kolPrzedmiot;
    @FXML
    private TableColumn<Obecnosc, String> kolData;
    @FXML
    private TableColumn<Obecnosc, String> kolWartosc;
    @FXML
    private Label userid;

    private String username = "uzytkownik";
    private Long pesel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        przekazNazweUzytkownikaIPesel(username, pesel);
        wstawUseraDoZalogowanoJako(username);
        tabelaNieob.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> {
            Utils.customResize(tabelaNieob);
            pesel = getPesel();
            wstawNieobecnosci();
        });
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    //ładujemy okno z ocenami uczenia.
    @FXML
    private void LoadNieobecnosci(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UczenNieobecnosci.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(UczenNieobecnosciController.class.getName()).log(Level.SEVERE, null, ex);
        }
        UczenNieobecnosciController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);
    }

    @FXML
    private void LoadOceny(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UczenOceny.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(UczenNieobecnosciController.class.getName()).log(Level.SEVERE, null, ex);
        }
        UczenOcenyController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenOceny.fxml"));
        //rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUwagi(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UczenUwagi.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(UczenNieobecnosciController.class.getName()).log(Level.SEVERE, null, ex);
        }
        UczenUwagiController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenUwagi.fxml"));
        //rootPane.getChildren().setAll(pane);
    }

    public void wstawNieobecnosci() {
        Uczen uczen = HibernateUtil.zwrocUcznia(pesel);
        kolData.setCellValueFactory(new PropertyValueFactory<>("data"));
        kolWartosc.setCellValueFactory(new PropertyValueFactory<>("wartosc"));
        kolPrzedmiot.setCellValueFactory(new Callback<CellDataFeatures<Obecnosc, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Obecnosc, String> p) {
                StringProperty nazwaPrzedmiotu = new SimpleStringProperty();
                nazwaPrzedmiotu.setValue(p.getValue().getPrzedmiot().getNazwaPrzedmiotu());
                return nazwaPrzedmiotu;
            }
        });
        Set nieobecnosciSet = uczen.getObecnoscs();
        ArrayList<Obecnosc> listaNieobecnosci = posortujNieobecnosci(nieobecnosciSet);

        ObservableList<Obecnosc> dane = FXCollections.observableArrayList(listaNieobecnosci);
        tabelaNieob.setItems(dane);
    }

    public ArrayList<Obecnosc> posortujNieobecnosci(Set nieobecnosciSet) {
        ArrayList<Obecnosc> obecnosci = new ArrayList<Obecnosc>();
        Iterator<Obecnosc> it = nieobecnosciSet.iterator();

        while (it.hasNext()) {
            Obecnosc ob = it.next();
            if (ob.getWartosc().equals("n")) {
                ob.setWartosc("nieobecny");
                obecnosci.add(ob);
            } else if (ob.getWartosc().equals("nr")) {
                ob.setWartosc("oczekujace");
                obecnosci.add(ob);
            } else if (ob.getWartosc().equals("u")) {
                ob.setWartosc("usprawiedliwione");
                obecnosci.add(ob);
            }
        }
        Comparator<Obecnosc> porownajPoGodzinie = (Obecnosc o1, Obecnosc o2)
                -> o1.getData().compareTo(o2.getData());
        Collections.sort(obecnosci, porownajPoGodzinie);

        return obecnosci;
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
