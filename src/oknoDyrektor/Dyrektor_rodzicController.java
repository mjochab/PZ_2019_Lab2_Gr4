/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoDyrektor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mapping.Klasa;
import mapping.Rodzic;
import mapping.Uczen;
import static utilities.HibernateUtil.edytujRodzicaNoweDane;
import static utilities.HibernateUtil.edytujUczniaNoweDane;
import static utilities.HibernateUtil.pobierzKlasy;
import static utilities.HibernateUtil.pobierzListePeseliRodzicow;
import static utilities.HibernateUtil.pobierzListePeseliUczniow;
import static utilities.HibernateUtil.podajPeseleRodzicaBezDanych;
import static utilities.HibernateUtil.podajPeseleUczniaBezDanych;
import static utilities.HibernateUtil.podajPeseleUczniaBezRodzica;
import static utilities.HibernateUtil.uzyskajLoginZalogowany;
import static utilities.HibernateUtil.wstawRodzica;
import static utilities.HibernateUtil.wstawUcznia;
import static utilities.HibernateUtil.zwrocAutoryzacje;
import static utilities.HibernateUtil.zwrocUcznia;
import static utilities.HibernateUtil.zwrocRodzica;

/**
 * FXML Controller class
 *
 * @author Kasia
 */
public class Dyrektor_rodzicController implements Initializable {

    @FXML
    private Button uczenbtn;
    @FXML
    private Button nauczycielbtn;
    @FXML
    private Button rodzicbtn;
    @FXML
    private Button wylogujbtn;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label userid;
    @FXML
    private TextField imie_u;
    @FXML
    private TextField nazwisko_u;
    @FXML
    private TextField imie_m;
    @FXML
    private TextField imie_o;
    @FXML
    private TextField nazwisko_m;
    @FXML
    private TextField nazwisko_o;
    @FXML
    private ChoiceBox pesel_u;
    @FXML
    private ChoiceBox pesel_r;
    @FXML
    private ChoiceBox ucz_pesel_r;
    @FXML
    private TextField e_imie_u;
    @FXML
    private TextField e_nazwisko_u;
    @FXML
    private TextField e_imie_m;
    @FXML
    private TextField e_imie_o;
    @FXML
    private TextField e_nazwisko_m;
    @FXML
    private TextField e_nazwisko_o;
    @FXML
    private TextField field_uczen_p;
    @FXML
    private TextField field_rodzic_p;
    @FXML
    private TextField pesel_r_e;
    @FXML
    private ChoiceBox e_pesel_u;
    @FXML
    private ChoiceBox e_pesel_r;
    @FXML
    private Label ucz_error;
    @FXML
    private Label rodzic_error;
    @FXML
    private Label label_e_rodzic;
    @FXML
    private Label label_e_uczen;
    @FXML
    private ChoiceBox klasa;
    @FXML
    private ChoiceBox e_klasa;
    @FXML
    private ChoiceBox edytuj_rodzicabox;

    private Long pesel = null;
    private String username = "rodzic";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ustawWartosciBox();
        wstawUseraDoZalogowanoJako(username);
        obslugaBoxEdycjiPeseluUcznia();
        obslugaBoxEdycjiPeseluRodzica();

    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadNauczyciel(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dyrektor.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(Dyrektor_rodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        DyrektorController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

    }

    @FXML
    private void LoadRodzic(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dyrektor_rodzic.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(Dyrektor_rodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_rodzicController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

    }

    @FXML
    private void LoadUczen(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dyrektor_Autoryzacja.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(Dyrektor_rodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_AutoryzacjaController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

    }

    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
        this.username = username;
        this.pesel = pesel;
    }

    public void przekazNazweUzytkownika(String username) {
        this.username = username;
    }

    public void wstawUseraDoZalogowanoJako(String username) {
        userid.setText(username);

    }

    private void ustawWartosciBox() {
        ustawWartosciPeseliLoginowUcznia();
        ustawWartosciPeseliLoginowRodzica();
        ustawWartosciPeseliRodzicaDlaUcznia();

        List<String> nazwy_klas = pobierzKlasy();
        ObservableList<String> nazwy_k = FXCollections.observableArrayList(nazwy_klas);
        klasa.setItems(nazwy_k);
        klasa.setValue(nazwy_k.get(0));

    }

    private void ustawWartosciBoxEdycja() {
        List<Long> peselki_u = pobierzListePeseliUczniow();
        ObservableList<Long> lista_u = FXCollections.observableArrayList(peselki_u);
        e_pesel_u.setItems(lista_u);
        if (peselki_u.size() > 0) {
            e_pesel_u.setValue(peselki_u.get(0));
            Uczen uczniak = zwrocUcznia(peselki_u.get(0));
            e_imie_u.setText(uczniak.getImie());
            e_nazwisko_u.setText(uczniak.getNazwisko());
            e_klasa.setValue(uczniak.getKlasa().getNazwaKlasy());
        }
        List<Long> peselki_r = pobierzListePeseliRodzicow();
        ObservableList<Long> lista_r = FXCollections.observableArrayList(peselki_r);
        e_pesel_r.setItems(lista_r);
        if (peselki_r.size() > 0) {
            e_pesel_r.setValue(peselki_r.get(0));
            Rodzic rodzic = zwrocRodzica(peselki_r.get(0));
            e_imie_o.setText(rodzic.getImieOjca());
            e_nazwisko_o.setText(rodzic.getNazwiskoOjca());
            e_imie_m.setText(rodzic.getImieMatki());
            e_nazwisko_m.setText(rodzic.getNazwiskoMatki());
        }

        ObservableList<String> nazwy_k = FXCollections.observableArrayList("1a", "1b", "1c");
        e_klasa.setItems(nazwy_k);
        e_klasa.setValue(nazwy_k.get(0));
    }

    private void obslugaBoxEdycjiPeseluUcznia() {
        ustawWartosciPeseliUczniaEdycja();
        List<String> nazwy_klas = pobierzKlasy();
        ObservableList<String> nazwy_k = FXCollections.observableArrayList(nazwy_klas);
        List<String> nazwy_rodzicow = zrobListeImieINazwiskoRodzica();
        ObservableList<String> nazwy_r = FXCollections.observableArrayList(nazwy_rodzicow);
        edytuj_rodzicabox.setItems(nazwy_r);
        e_klasa.setItems(nazwy_k);
        e_pesel_u.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String poprzednia, String nowa) {
                int indeks = e_pesel_u.getSelectionModel().getSelectedIndex();
                List<Long> pesele_ucznia = pobierzListePeseliUczniow();
                Long peselek = pesele_ucznia.get(indeks);
                Uczen uczniak = zwrocUcznia(peselek);
                String pesel_rodzica = uczniak.getRodzic().getPesel() + "";
                e_imie_u.setText(uczniak.getImie());
                e_nazwisko_u.setText(uczniak.getNazwisko());
                edytuj_rodzicabox.setValue(uczniak.getRodzic().getImieMatki() + " " + uczniak.getRodzic().getNazwiskoMatki());
                e_klasa.setValue(uczniak.getKlasa().getNazwaKlasy());
                field_uczen_p.setText(peselek.toString());
                label_e_uczen.setText("");
            }
        });
    }

    private void obslugaBoxEdycjiPeseluRodzica() {
        ustawWartosciPeseliRodzicaEdycja();
        e_pesel_r.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String poprzednia, String String) {
                int indeks = e_pesel_r.getSelectionModel().getSelectedIndex();
                List<Long> pesele_rodzica = pobierzListePeseliRodzicow();
                Long peselek = pesele_rodzica.get(indeks);
                Rodzic rodzic = zwrocRodzica(peselek);
                String pesel_rodz = rodzic.getPesel() + "";
                e_imie_o.setText(rodzic.getImieOjca());
                e_nazwisko_o.setText(rodzic.getNazwiskoOjca());
                e_imie_m.setText(rodzic.getImieMatki());
                e_nazwisko_m.setText(rodzic.getNazwiskoMatki());
                pesel_r_e.setText(pesel_rodz);
                label_e_rodzic.setText("");
            }
        });
    }

    @FXML
    private void wstawNowegoUcznia() {
        Klasa klasa_U = new Klasa(klasa.getSelectionModel().getSelectedItem().toString());
        if (imie_u.getText().isEmpty() || nazwisko_u.getText().isEmpty()) {
            ucz_error.setText("Niepoprawne dane!");
        } else {
            try {
                int indeks = pesel_u.getSelectionModel().getSelectedIndex();
                int indeks2 = ucz_pesel_r.getSelectionModel().getSelectedIndex();
                List<Long> pesele_ucznia = podajPeseleUczniaBezDanych();
                Long peselek = pesele_ucznia.get(indeks);
                List<Long> pesele_rodzica = pobierzListePeseliRodzicow();
                Long peselek_r = pesele_rodzica.get(indeks2);

                wstawUcznia(peselek, peselek_r, imie_u.getText(), nazwisko_u.getText(), klasa_U);
                ucz_error.setText("Dodano ucznia!");
                imie_u.setText("");
                nazwisko_u.setText("");
                ustawWartosciBox();
                obslugaBoxEdycjiPeseluUcznia();
                obslugaBoxEdycjiPeseluRodzica();
            } catch (Exception e) {
                ucz_error.setText("Niepoprawny pesel!");
            }

        }

    }

    @FXML
    private void wstawNowegoRodzica() {
        if (imie_o.getText().isEmpty() || nazwisko_o.getText().isEmpty() || imie_m.getText().isEmpty() || nazwisko_m.getText().isEmpty()) {
            rodzic_error.setText("Niepoprawne dane!");
        } else {
            try {
                int indeks = pesel_r.getSelectionModel().getSelectedIndex();
                //Long peselR = Long.parseLong(pesel_r.getSelectionModel().getSelectedItem().toString());
                List<Long> pesele_rodzica = podajPeseleRodzicaBezDanych();
                Long peselek = pesele_rodzica.get(indeks);
                wstawRodzica(peselek, imie_o.getText(), nazwisko_o.getText(), imie_m.getText(), nazwisko_m.getText());
                rodzic_error.setText("Dodano rodzica!");
                imie_m.setText("");
                nazwisko_m.setText("");
                imie_o.setText("");
                nazwisko_o.setText("");
                ustawWartosciBox();
                obslugaBoxEdycjiPeseluUcznia();
                obslugaBoxEdycjiPeseluRodzica();
            } catch (Exception e) {
                rodzic_error.setText("Niepoprawny pesel!");
            }
        }
    }

    @FXML
    private void edytujRodzica() {
        int indeks = e_pesel_r.getSelectionModel().getSelectedIndex();
        List<Long> pesele_rodzica = pobierzListePeseliRodzicow();
        Long pesel_rodzic = pesele_rodzica.get(indeks);

        edytujRodzicaNoweDane(pesel_rodzic, e_imie_o.getText(), e_nazwisko_o.getText(), e_imie_m.getText(), e_nazwisko_m.getText());
        label_e_rodzic.setText("Edytowano!");
    }

    @FXML
    private void edytujUcznia() {
        Klasa klasa_U = new Klasa(e_klasa.getSelectionModel().getSelectedItem().toString());
        int indeks = e_pesel_u.getSelectionModel().getSelectedIndex();
        List<Long> pesele_uczniow = pobierzListePeseliUczniow();
        Long pesel_uczniak = pesele_uczniow.get(indeks);
        int indeks_r = edytuj_rodzicabox.getSelectionModel().getSelectedIndex();
        List<Long> pesele_rodzicow = pobierzListePeseliRodzicow();
        Long pesel_rodz = pesele_rodzicow.get(indeks_r);

        edytujUczniaNoweDane(pesel_uczniak,pesel_rodz, e_imie_u.getText(), e_nazwisko_u.getText(),klasa_U);
        label_e_uczen.setText("Edytowano!");
    }

    private void ustawWartosciPeseliLoginowUcznia() {
        List<String> peselki = zrobListePeseliILoginowUcznia();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        pesel_u.setItems(lista);
        if (peselki.size() > 0) {
            pesel_u.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListePeseliILoginowUcznia() {
        List<Long> peselki = podajPeseleUczniaBezDanych();
        List<String> loginy = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            String log = uzyskajLoginZalogowany(peselki.get(i));
            loginy.add(peselki.get(i) + " - " + log);
        }
        return loginy;
    }

    private void ustawWartosciPeseliLoginowRodzica() {
        List<String> peselki = zrobListePeseliILoginowRodzica();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        pesel_r.setItems(lista);
        if (peselki.size() > 0) {
            pesel_r.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListePeseliILoginowRodzica() {
        List<Long> peselki = podajPeseleRodzicaBezDanych();
        List<String> loginy = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            String log = uzyskajLoginZalogowany(peselki.get(i));
            loginy.add(peselki.get(i) + " - " + log);
        }
        return loginy;
    }

    private void ustawWartosciPeseliRodzicaDlaUcznia() {
        List<String> peselki = zrobListePeseliILoginowRodzicadlaUcznia();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        ucz_pesel_r.setItems(lista);
        if (peselki.size() > 0) {
            ucz_pesel_r.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListePeseliILoginowRodzicadlaUcznia() {
        List<Long> peselki = pobierzListePeseliRodzicow();
        List<String> loginy = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            String log = uzyskajLoginZalogowany(peselki.get(i));
            loginy.add(peselki.get(i) + " - " + log);
        }
        return loginy;
    }

    private void ustawWartosciPeseliUczniaEdycja() {
        List<String> peselki = zrobListeImieINazwiskoUcznia();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        e_pesel_u.setItems(lista);
        if (peselki.size() > 0) {
            //e_pesel_u.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListeImieINazwiskoUcznia() {
        List<Long> peselki = pobierzListePeseliUczniow();
        List<String> przedstawienie = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            Uczen uczniak = zwrocUcznia(peselki.get(i));
            String imie = uczniak.getImie();
            String nazwisko = uczniak.getNazwisko();
            przedstawienie.add(imie + " " + nazwisko);
        }
        return przedstawienie;
    }

    private void ustawWartosciPeseliRodzicaEdycja() {
        List<String> peselki = zrobListeImieINazwiskoRodzica();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        e_pesel_r.setItems(lista);
        if (peselki.size() > 0) {
            // e_pesel_r.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListeImieINazwiskoRodzica() {
        List<Long> peselki = pobierzListePeseliRodzicow();
        List<String> przedstawienie = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            Rodzic rodzic = zwrocRodzica(peselki.get(i));
            String imie = rodzic.getImieMatki();
            String nazwisko = rodzic.getNazwiskoMatki();
            przedstawienie.add(imie + " " + nazwisko);
        }
        return przedstawienie;
    }

}
