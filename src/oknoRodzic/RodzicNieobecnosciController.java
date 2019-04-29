/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoRodzic;

import oknoUczen.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
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
import utilities.Utils;

public class RodzicNieobecnosciController implements Initializable {

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

    private final long PESEL = 32222222221L;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wstawNieobecnosci();
        tabelaNieob.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaNieob));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {

    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    //ładujemy okno z ocenami uczenia.
    @FXML
    private void LoadNieobecnosci(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenNieobecnosci.fxml"));
        rootPane.getChildren().setAll(pane);
        tabelaNieob.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaNieob));
        wstawNieobecnosci();
    }

    @FXML
    private void LoadOceny(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenOceny.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUwagi(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenUwagi.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void wstawNieobecnosci() {
        Uczen uczen = HibernateUtil.zwrocUcznia(PESEL);
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
            if (ob.getWartosc().equals("nieobecny")) {
                obecnosci.add(ob);
            }
        }
        Comparator<Obecnosc> porownajPoGodzinie = (Obecnosc o1, Obecnosc o2)
                -> o1.getData().compareTo(o2.getData());
        Collections.sort(obecnosci, porownajPoGodzinie);

        return obecnosci;
    }

}
