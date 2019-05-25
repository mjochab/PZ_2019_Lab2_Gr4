/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoDyrektor;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mapping.Klasa;
import mapping.Nauczyciel;
import mapping.Przedmiot;
import mapping.Zajecia;
import static utilities.HibernateUtil.dodajNowaKlase;
import static utilities.HibernateUtil.dodajNowyPrzedmiot;
import static utilities.HibernateUtil.dodajZajecia;
import static utilities.HibernateUtil.edytujKlaseZNowymiDanymi;
import static utilities.HibernateUtil.pobierzKlasePoNazwie;
import static utilities.HibernateUtil.pobierzKlasy;
import static utilities.HibernateUtil.pobierzListePeseliNauczycieli;
import static utilities.HibernateUtil.pobierzListePrzedmiotow;
import static utilities.HibernateUtil.pobierzPrzedmiotPoNazwie;
import static utilities.HibernateUtil.podajPeseleNauczycielaBezWychowankow;
import static utilities.HibernateUtil.sprawdzCzyKlasaJestPowiazana;
import static utilities.HibernateUtil.sprawdzCzyPrzedmiotJestPowiazany;
import static utilities.HibernateUtil.usunKlaseJesliNieMaPowiazan;
import static utilities.HibernateUtil.usunPrzedmiot;
import static utilities.HibernateUtil.zwrocAutoryzacje;
import static utilities.HibernateUtil.zwrocNauczyciela;
import static utilities.HibernateUtil.podajListeWolnychGodzDanegoDniaDlaDanejKlasy;
import static utilities.HibernateUtil.sprawdzCzyNauczycielNieMaZajecWDniuGodz;

/**
 * FXML Controller class
 *
 * @author Kasia
 */
public class HarmonogramController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label userid;
    @FXML
    private Label label_dodawanie_klasy;
    @FXML
    private Label label_m_klasa;
    @FXML
    private TextField field_nazwa_klasy;
    @FXML
    private DatePicker rok_szkolny;
    @FXML
    private DatePicker data_szkoly;
    @FXML
    private ChoiceBox box_wychowawca;
    @FXML
    private ChoiceBox box_klasa_m;
    @FXML
    private ChoiceBox box_wychowawca_m;
    @FXML
    private TextField field_przedmiot;
    @FXML
    private Label label_przedmiot_d;
    @FXML
    private Label label_przedmiot_u;
    @FXML
    private Label zajecia_label;
    @FXML
    private Label naucz_error;
    @FXML
    private ChoiceBox box_przedmiot_d;
    @FXML
    private ChoiceBox d_box_klasa;
    @FXML
    private ChoiceBox d_box_prowadz;
    @FXML
    private ChoiceBox d_box_przedmiot;
    @FXML
    private ChoiceBox d_box_dzien;
    @FXML
    private ChoiceBox d_box_godz;

    private Long pesel = null;
    private String username = "harmonogram";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ustawWartosciBox();
        ustawBoxPrzedmiotyUsuwanie();
        ustawWartosciPeseliWychowawcaNowaKlasa();
        ustawWartosciBoxDodawanieZajec();
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
            Logger.getLogger(HarmonogramController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HarmonogramController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HarmonogramController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_AutoryzacjaController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

    }

    @FXML
    private void LoadHarmonogram(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Harmonogram.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(HarmonogramController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HarmonogramController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

    }

    /**
     * Metoda przekazuje nazwę użytkownika i pesel.
     *
     * @param username - nazwa użytkownika typu String
     * @param pesel - pesel użytkownika typu long
     */
    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
        this.username = username;
        this.pesel = pesel;
    }

    /**
     * Metoda przekazuje nazwę użytkownika.
     *
     * @param username - nazwa użytkownika typu String
     */
    public void przekazNazweUzytkownika(String username) {
        this.username = username;
    }

    /**
     * Metoda wstawia do pola nazwę użytkownika przekazaną jako argument.
     *
     * @param username - nazwa użytkownika typu String
     */
    public void wstawUseraDoZalogowanoJako(String username) {
        userid.setText(username);
    }

    private void ustawWartosciBox() {
        List<String> nazwy_klas = pobierzKlasy();
        List<String> wychowawcy_lista = zrobListeImieINazwiskoNauczycielaBezWychowankow();
        if (wychowawcy_lista.size() > 0) {
            wychowawcy_lista.add("");
        }
        ObservableList<String> nazwy_k = FXCollections.observableArrayList(nazwy_klas);
        ObservableList<String> wychowawcy_dane = FXCollections.observableArrayList(wychowawcy_lista);
        box_klasa_m.setItems(nazwy_k);
        box_wychowawca_m.setItems(wychowawcy_dane);
        box_klasa_m.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String poprzednia, String nowa) {
                try {
                    int indeks = box_klasa_m.getSelectionModel().getSelectedIndex();
                    List<String> klasa_lista = pobierzKlasy();
                    String klasa_naz = klasa_lista.get(indeks);
                    Klasa pobranaKlasa = pobierzKlasePoNazwie(klasa_naz);
                    data_szkoly.setValue(Instant.ofEpochMilli(pobranaKlasa.getRokSzkolny().getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    Nauczyciel wychowawca = pobranaKlasa.getNauczyciel();
                    String dane = wychowawca.getImie() + " " + wychowawca.getNazwisko();
                    wychowawcy_dane.set(wychowawcy_lista.size() - 1, dane);
                    box_wychowawca_m.setValue(dane);
                } catch (Exception e) {

                }
            }
        });

    }

    @FXML
    private void wstawKlase() {
        Klasa klasa = new Klasa();

        int indeks_r = box_wychowawca.getSelectionModel().getSelectedIndex();
        List<Long> pesele_rodzicow = podajPeseleNauczycielaBezWychowankow();
        if (indeks_r != -1 && pesele_rodzicow.size() > 0 && !field_nazwa_klasy.getText().isEmpty()) {
            Long pesel_rodz = pesele_rodzicow.get(indeks_r);
            Nauczyciel nauczyciel = zwrocNauczyciela(pesel_rodz);
            klasa.setNauczyciel(nauczyciel);
            klasa.setNazwaKlasy(field_nazwa_klasy.getText());
            try {
                Date data_szkolna = java.sql.Date.valueOf(rok_szkolny.getValue());
                klasa.setRokSzkolny(data_szkolna);

                dodajNowaKlase(klasa);
                ustawWartosciPeseliWychowawcaNowaKlasa();
                field_nazwa_klasy.setText("");
                label_dodawanie_klasy.setText("Dodano nową klasę!");
                ustawWartosciBox();
            } catch (Exception e) {
                label_dodawanie_klasy.setText("Niepoprawne dane!");
            }
        } else {
            label_dodawanie_klasy.setText("Niepoprawne dane!");
        }
    }

    @FXML
    private void edytujKlase() {
        try {
            box_wychowawca_m.getValue();
            Date data_szkolna = java.sql.Date.valueOf(data_szkoly.getValue());
            int indeks = box_klasa_m.getSelectionModel().getSelectedIndex();
            List<String> klasa_lista = pobierzKlasy();
            String klasa_naz = klasa_lista.get(indeks);
            Klasa pobranaKlasa = pobierzKlasePoNazwie(klasa_naz);

            int indeks_n = box_wychowawca_m.getSelectionModel().getSelectedIndex();
            List<Long> naucz_lista = podajPeseleNauczycielaBezWychowankow();
            if (indeks_n < naucz_lista.size()) {
                Nauczyciel nauczyciel = zwrocNauczyciela(naucz_lista.get(indeks_n));
                pobranaKlasa.setRokSzkolny(data_szkolna);
                pobranaKlasa.setNauczyciel(nauczyciel);
                edytujKlaseZNowymiDanymi(pobranaKlasa);
                ustawWartosciBox();
                ustawWartosciPeseliWychowawcaNowaKlasa();
                label_m_klasa.setText("Pomyślnie edytowano!");
            } else {
                pobranaKlasa.setRokSzkolny(data_szkolna);
                edytujKlaseZNowymiDanymi(pobranaKlasa);
                ustawWartosciBox();
                label_m_klasa.setText("Pomyślnie edytowano!");
            }
            //String klasa_naz = klasa_lista.get(indeks);

        } catch (Exception e) {
            label_m_klasa.setText("Niepoprawne dane!");
        }
    }

    private void ustawWartosciPeseliWychowawcaNowaKlasa() {
        List<String> peselki = zrobListeImieINazwiskoNauczycielaBezWychowankow();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        box_wychowawca.setItems(lista);
    }

    private List<String> zrobListeImieINazwiskoNauczycielaBezWychowankow() {
        List<Long> peselki = podajPeseleNauczycielaBezWychowankow();
        List<String> przedstawienie = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            Nauczyciel nauczyciel = zwrocNauczyciela(peselki.get(i));
            String imie = nauczyciel.getImie();
            String nazwisko = nauczyciel.getNazwisko();
            przedstawienie.add(imie + " " + nazwisko);
        }
        return przedstawienie;
    }

    @FXML
    private void usunKlase() {
        try {
            int indeks = box_klasa_m.getSelectionModel().getSelectedIndex();
            List<String> klasa_lista = pobierzKlasy();
            String klasa_naz = klasa_lista.get(indeks);
            Klasa pobranaKlasa = pobierzKlasePoNazwie(klasa_naz);
            if (sprawdzCzyKlasaJestPowiazana(pobranaKlasa)) {
                label_m_klasa.setText("Nie można usunąć!");
            } else {
                pobranaKlasa.setNauczyciel(null);
                pobranaKlasa.setZajecias(null);
                pobranaKlasa.setSkladKlasies(null);
                pobranaKlasa.setUczens(null);
                usunKlaseJesliNieMaPowiazan(pobranaKlasa);
                label_m_klasa.setText("Usunięto!");
                ustawWartosciBox();
                ustawWartosciPeseliWychowawcaNowaKlasa();
            }
        } catch (Exception e) {
            label_m_klasa.setText("Niepoprawne dane!");
        }
    }

    @FXML
    private void dodajPrzedmiotDoBazy() {
        try {
            String nazwa_prz = field_przedmiot.getText();
            if (nazwa_prz.isEmpty()) {
                label_przedmiot_d.setText("Proszę podać nazwę!");
            } else {
                Przedmiot nowy = new Przedmiot(nazwa_prz);
                dodajNowyPrzedmiot(nowy);
                label_przedmiot_d.setText("Dodano!");
                ustawBoxPrzedmiotyUsuwanie();
            }
        } catch (Exception e) {
            label_przedmiot_d.setText("Niepoprawne dane!");
        }
    }

    private void ustawBoxPrzedmiotyUsuwanie() {
        List<String> przedmiociki = pobierzListePrzedmiotow();
        ObservableList<String> lista = FXCollections.observableArrayList(przedmiociki);
        box_przedmiot_d.setItems(lista);
    }

    @FXML
    private void usunPrzedmiotZBazy() {
        try {
            String nazwa_prz = box_przedmiot_d.getSelectionModel().getSelectedItem().toString();
            Przedmiot przed = pobierzPrzedmiotPoNazwie(nazwa_prz);
            if (sprawdzCzyPrzedmiotJestPowiazany(przed)) {
                label_przedmiot_u.setText("Nie można usunąć!");
            } else {
                przed.setObecnoscs(null);
                przed.setOcenas(null);
                przed.setZajecias(null);
                usunPrzedmiot(przed);
                label_przedmiot_u.setText("Usunięto!");
                ustawBoxPrzedmiotyUsuwanie();
            }
        } catch (Exception e) {
            label_przedmiot_u.setText("Niepoprawne dane!");
        }
    }

    private void ustawWartosciBoxDodawanieZajec() {
        //List<String> godzinyZajec = List.of("8:00:00", "8:55:00", "9:50:00", "10:45:00", "11:45:00", "12:40:00", "13:35:00", "14:30:00");
        List<String> klasy = pobierzKlasy();
        List<String> dniZajec = List.of("pon", "wt", "sr", "czw", "pt");
        List<String> przedmioty = pobierzListePrzedmiotow();
        ObservableList<String> listaPrzedmiotow = FXCollections.observableArrayList(przedmioty);
        ObservableList<String> listaKlas = FXCollections.observableArrayList(klasy);
        ObservableList<String> listaDni = FXCollections.observableArrayList(dniZajec);
        d_box_przedmiot.setItems(listaPrzedmiotow);
        d_box_klasa.setItems(listaKlas);
        d_box_dzien.setItems(listaDni);
        ustawWartosciPeseliNuczyciela();

        d_box_klasa.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String poprzednia, String String) {
                zajecia_label.setText("");
                naucz_error.setText("");
                int indeks = d_box_klasa.getSelectionModel().getSelectedIndex();
                d_box_dzien.setItems(listaDni);
                
                if (indeks > -1) {
                    String klas = klasy.get(indeks);
                    Klasa klasa = pobierzKlasePoNazwie(klas);
                    d_box_dzien.valueProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue ov, String poprzednia, String String) {
                            int indeks_dzien = d_box_dzien.getSelectionModel().getSelectedIndex();                       
                            if (indeks_dzien > -1) {
                                String dzien = dniZajec.get(indeks_dzien);
                                List<String> listaWolnychGodz = podajListeWolnychGodzDanegoDniaDlaDanejKlasy(dzien, klasa);
                                ObservableList<String> listaGodzin = FXCollections.observableArrayList(listaWolnychGodz);
                                d_box_godz.setItems(listaGodzin);
                            }

                        }
                    });
                }

            }
        });
    }

    private void ustawWartosciPeseliNuczyciela() {
        List<String> peselki = zrobListeImieINazwiskoNauczyciela();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        d_box_prowadz.setItems(lista);
        if (peselki.size() > 0) {
            // e_pesel_r.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListeImieINazwiskoNauczyciela() {
        List<Long> peselki = pobierzListePeseliNauczycieli();
        List<String> przedstawienie = new ArrayList<>();
        for (int i = 0; i < peselki.size(); i++) {
            Nauczyciel naucz = zwrocNauczyciela(peselki.get(i));
            String imie = naucz.getImie();
            String nazwisko = naucz.getNazwisko();
            przedstawienie.add(imie + " " + nazwisko);
        }
        return przedstawienie;
    }

    @FXML
    private void dodajNoweZajecia() throws ParseException {
        //Zajecia(Klasa klasa, Nauczyciel nauczyciel, Przedmiot przedmiot, Date godzina, String dzien)
        try{
        int prz_indeks = d_box_przedmiot.getSelectionModel().getSelectedIndex();
        List<String> przedmioty = pobierzListePrzedmiotow();
        Przedmiot przedmiot_z_listy = pobierzPrzedmiotPoNazwie(przedmioty.get(prz_indeks));
        String dzien = d_box_dzien.getSelectionModel().getSelectedItem().toString();
        String klasa_wybrana = d_box_klasa.getSelectionModel().getSelectedItem().toString();
        Klasa klasa = pobierzKlasePoNazwie(klasa_wybrana);
        int naucz_indeks = d_box_prowadz.getSelectionModel().getSelectedIndex();
        List<Long> nauczyciele = pobierzListePeseliNauczycieli();
        Nauczyciel nauczyciel_z_listy = zwrocNauczyciela(nauczyciele.get(naucz_indeks));
        String time = d_box_godz.getSelectionModel().getSelectedItem().toString();
        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date date = sdf.parse(time);

        if (sprawdzCzyNauczycielNieMaZajecWDniuGodz(nauczyciel_z_listy, dzien, date)) {
            naucz_error.setText("Wybrany nauczyciel prowadzi już zajecia!");
            zajecia_label.setText("");
        } else {
            Zajecia zajecia = new Zajecia(klasa, nauczyciel_z_listy, przedmiot_z_listy, date, dzien);
            dodajZajecia(zajecia);
            ustawWartosciBoxDodawanieZajec();
            naucz_error.setText("");
            zajecia_label.setText("Dodano!");
        }

        }catch(Exception e){
            zajecia_label.setText("Proszę podać dane!");
        }
    }

}
