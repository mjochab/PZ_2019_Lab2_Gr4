/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoRodzic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import mapping.Klasa;
import mapping.Obecnosc;
import mapping.Przedmiot;
import mapping.Uczen;
import mapping.Zajecia;
import utilities.HibernateUtil;
import static utilities.HibernateUtil.*;
import utilities.Utils;

public class RodzicPlanController implements Initializable {

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
    private TableView tabelaZajec;
    @FXML
    private TableColumn<Integer, String> godzina;
    @FXML
    private TableColumn<Integer, String> pon;
    @FXML
    private TableColumn<Integer, String> wt;
    @FXML
    private TableColumn<Integer, String> sr;
    @FXML
    private TableColumn<Integer, String> czw;
    @FXML
    private TableColumn<Integer, String> pt;

    private final long PESEL = 32222222221L;
    private ObservableList<TableColumn> kolumna;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wstawPlan();

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
        AnchorPane pane = FXMLLoader.load(getClass().getResource("RodzicNieobecnosci.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadOceny(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Rodzic.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUwagi(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("RodzicPlan.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void ustawIdKolumn() {
        pon.setId("pon");
        wt.setId("wt");
        sr.setId("sr");
        czw.setId("czw");
        pt.setId("pt");
    }

    public void wstawPlan() {
        Uczen uczen = HibernateUtil.zwrocUcznia(PESEL);
        Klasa plan = zwrocPlan(uczen.getKlasa().getNazwaKlasy());
        Set zajecia = plan.getZajecias();
        ArrayList<Zajecia> zajeciaPosortowane = Utils.posortujZajecia(zajecia);
        kolumna = tabelaZajec.getColumns();
        ArrayList<String> zajeciaDnia;

        for (int i = 0; i < zajecia.size(); i++) {
            tabelaZajec.getItems().add(i);
        }
        
        ArrayList<String> godziny = Utils.pobierzGodziny(zajeciaPosortowane);
        Utils.wstawianieGodziny(godziny, godzina);
  
        for (TableColumn<Integer, String> kol : kolumna) {
            if (kol.getText().equals("Godzina")) {

            } else {
                zajeciaDnia = Utils.pobierzZajeciaDnia(kol.getId(), zajeciaPosortowane, godziny);
                Utils.wstawianieZajecDoKolumn(kol, zajeciaDnia);
            }
        }
    }

    
}
