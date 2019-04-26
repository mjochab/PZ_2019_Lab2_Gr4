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
import java.util.List;
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

public class UczenUwagiController implements Initializable {

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
        AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenNieobecnosci.fxml"));
        rootPane.getChildren().setAll(pane);
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
        System.out.println(uczen.getKlasa().getNazwaKlasy());
        Set zajecia = plan.getZajecias();

        ArrayList<Zajecia> zajeciaPosortowane = posortujZajecia(zajecia);

        kolumna = tabelaZajec.getColumns();
        ArrayList<String> zajeciaDnia;

        for (int i = 0; i < zajecia.size(); i++) {
            tabelaZajec.getItems().add(i);
        }
        ArrayList<String> godziny = pobierzGodziny(zajeciaPosortowane);
        wstawianieGodziny(godziny, godzina);
        Iterator<Zajecia> itg = zajeciaPosortowane.iterator();
        while (itg.hasNext()) {
           Zajecia o = itg.next();
            System.out.println(o.getDzien()+" "+o.getPrzedmiot().getNazwaPrzedmiotu()+" "+o.getGodzina().toString());
        }
        for (TableColumn<Integer, String> kol : kolumna) {
            if (kol.getText().equals("Godzina")) {

            } else {
                zajeciaDnia = pobierzZajeciaDnia(kol.getId(), zajeciaPosortowane, godziny);
                System.out.println(kol.getId());
                System.out.println(zajeciaDnia.toString());
                System.out.println("----------------");
                wstawianieZajecDoKolumn(kol, zajeciaDnia);
            }
        }
    }

    public void wstawianieGodziny(ArrayList<String> godzina, TableColumn<Integer, String> kol) {
        kol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            if (rowIndex >= godzina.size()) {
                return null;
            } else {
                return new ReadOnlyStringWrapper(godzina.get(rowIndex));
            }
        });
    }

    public ArrayList<String> pobierzGodziny(ArrayList<Zajecia> zajecia) {
        ArrayList<String> lista = new ArrayList<String>();
        Iterator<Zajecia> it = zajecia.iterator();

        while (it.hasNext()) {
            Zajecia ob = it.next();
            if (lista.contains(ob.getGodzina().toString())) {
                continue;
            } else {
                lista.add(ob.getGodzina().toString());
            }
        }
        return lista;
    }

    public ArrayList<Zajecia> posortujZajecia(Set zajecia) {
        ArrayList<Zajecia> zajeciaPosortowane = new ArrayList<Zajecia>();

        Iterator<Zajecia> it = zajecia.iterator();
        while (it.hasNext()) {
            Zajecia ob = it.next();
            zajeciaPosortowane.add(ob);
        }
        Comparator<Zajecia> porownajPoGodzinie = (Zajecia o1, Zajecia o2)
                -> o1.getGodzina().compareTo(o2.getGodzina());

        Collections.sort(zajeciaPosortowane, porownajPoGodzinie);
        return zajeciaPosortowane;
    }

    public void wstawianieZajecDoKolumn(TableColumn<Integer, String> kol, ArrayList<String> dzien) {
        kol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            if (rowIndex >= dzien.size()) {
                return null;
            } else {
                return new ReadOnlyStringWrapper(dzien.get(rowIndex));
            }
        });
    }

    public ArrayList<String> pobierzZajeciaDnia(String dzien, ArrayList<Zajecia> zajeciaPosortowane, ArrayList<String> godzina) {
        ArrayList<String> zajeciaDnia = new ArrayList<String>();
        Iterator<Zajecia> it = zajeciaPosortowane.iterator();
        Iterator<String> itG = godzina.iterator();
        while (it.hasNext() && itG.hasNext()) {
            Zajecia ob = it.next();
            String x = itG.next();

            if (ob.getDzien().equals(dzien) && ob.getGodzina().toString().equals(x)) {
                System.out.println(x + ", " + ob.getGodzina().toString());
                zajeciaDnia.add(ob.getPrzedmiot().getNazwaPrzedmiotu());
            } else if (ob.getDzien().equals(dzien)) {
                zajeciaDnia.add(null);
            }
        }
        return zajeciaDnia;
    }
}
