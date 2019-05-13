/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoDyrektor;

import Okna.LogowanieController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import mapping.Nauczyciel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import static utilities.HibernateUtil.edytujNauczyciela;
import static utilities.HibernateUtil.pobierzListePeseliNauczycieli;
import static utilities.HibernateUtil.podajPeseleNauczycielaBezDanych;
import static utilities.HibernateUtil.usunAutoryzacjeNauczyciela;
import static utilities.HibernateUtil.uzyskajLoginZalogowany;
import static utilities.HibernateUtil.uzyskajPesel;
import static utilities.HibernateUtil.wstawAutoryzacje;
import static utilities.HibernateUtil.wstawNauczyciela;
import static utilities.HibernateUtil.zwrocNauczyciela;

/**
 * FXML Controller class
 *
 * @author Kasia
 */
public class DyrektorController implements Initializable {

    @FXML
    private Button autoryzacjabtn;
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
    private Label err_label;
    @FXML
    private Label label_login;
    @FXML
    private Label label_edycja;
    @FXML
    private Label label_usuwanie;
    @FXML
    private TextField imie_n;
    @FXML
    private TextField nazwisko_n;
    @FXML
    private ChoiceBox pesel_n;
    @FXML
    private ChoiceBox box_nauczycieli;
    @FXML
    private TextField e_imie_n;
    @FXML
    private TextField e_nazwisko_n;
    @FXML
    private TextField pesel_edycja;

    @FXML
    private TableView<Nauczyciel> tabela_nauczycieli;
    @FXML
    private TableColumn<Nauczyciel, Long> kol_pesel;
    @FXML
    private TableColumn<Nauczyciel, String> kol_imie;
    @FXML
    private TableColumn<Nauczyciel, String> kol_nazwisko;

    private ObservableList<Nauczyciel> data;

    private String username = "xd";
    private Long peselN = null;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ustawWartosciPeseliLoginow();
        wstawUseraDoZalogowanoJako(username);
        wstawienieDoTabeli();
        wstawianieDanychNauczycielaDoTextField();
        ustawWartosciPeseliRodzicaEdycja();
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
            Logger.getLogger(DyrektorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        DyrektorController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, peselN);

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
            Logger.getLogger(DyrektorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_rodzicController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, peselN);

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
            Logger.getLogger(DyrektorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_AutoryzacjaController controller = fxmlLoader.getController();

    }

     /**
     * Metoda wstawia do pola nazwę użytkownika przekazaną jako argument.
     * @param username - nazwa użytkownika typu String
     */
    public void wstawUseraDoZalogowanoJako(String username) {
        userid.setText(username);
    }

    /**
     * Metoda przekazuje nazwę użytkownika i pesel.
     * @param username - nazwa użytkownika typu String
     * @param pesel - pesel użytkownika typu long
     */
    public void przekazNazweUzytkownikaIPesel(String username, Long nr_pesel) {
        this.username = username;
        peselN = nr_pesel;
    }

    /**
     * Metoda przekazuje nazwę użytkownika.
     * @param username - nazwa użytkownika typu String
     */
    public void przekazNazweUzytkownika(String username) {
        this.username = username;
    }

    private Long getPesel() {
        String login = userid.getText();
        return uzyskajPesel(login);
    }


    private void ustawWartosciBox() {
        List<Long> peselki = podajPeseleNauczycielaBezDanych();
        ObservableList<Long> lista = FXCollections.observableArrayList(peselki);
        pesel_n.setItems(lista);
        if (peselki.size() > 0) {
            pesel_n.setValue(peselki.get(0));
            label_login.setText(uzyskajLoginZalogowany(peselki.get(0)));

            pesel_n.valueProperty().addListener(new ChangeListener<Long>() {
                @Override
                public void changed(ObservableValue ov, Long poprzednia, Long nowa) {
                    label_login.setText(uzyskajLoginZalogowany(nowa));
                }
            });
        }
    }

    private void ustawWartosciPeseliLoginow() {
        List<String> peselki = zrobListePeseliILoginow();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        pesel_n.setItems(lista);
        if (peselki.size() > 0) {
            pesel_n.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListePeseliILoginow() {
        List<Long> peselki = podajPeseleNauczycielaBezDanych();
        List<String> loginy = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            String log = uzyskajLoginZalogowany(peselki.get(i));
            loginy.add(peselki.get(i) + " - " + log);
        }
        return loginy;
    }

    @FXML
    private void wstawNowegoNauczyciela() {
        String imie = imie_n.getText();
        String nazwisko = nazwisko_n.getText();
        Long peselek = null;
        List<Long> pesele_naucz = podajPeseleNauczycielaBezDanych();
        int indeks = pesel_n.getSelectionModel().getSelectedIndex();

        if (imie.isEmpty() || nazwisko.isEmpty()) {
            err_label.setText("Niepoprawne dane!");
        } else {
            try {
                peselek = pesele_naucz.get(indeks);
                wstawNauczyciela(peselek, imie, nazwisko);
                imie_n.setText("");
                nazwisko_n.setText("");
                err_label.setText("Dodano do bazy!");
                ustawWartosciPeseliLoginow();
            } catch (Exception e) {
                err_label.setText("Niepoprawne dane!");
            }
        }
        ;
    }

    private void wstawienieDoTabeli() {
        kol_pesel.setCellValueFactory(new PropertyValueFactory<Nauczyciel, Long>("pesel")); // here id is a variable name which is define in pojo.
        kol_imie.setCellValueFactory(new PropertyValueFactory<Nauczyciel, String>("imie"));
        kol_nazwisko.setCellValueFactory(new PropertyValueFactory<Nauczyciel, String>("nazwisko"));

        data = FXCollections.observableArrayList();
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session sess = sf.openSession();
        Query qee = sess.createQuery("from Nauczyciel");
        Iterator ite = qee.iterate();
        while (ite.hasNext()) {
            Nauczyciel obj = (Nauczyciel) ite.next();

            data.add(obj);
        }
        tabela_nauczycieli.setItems(data);
    }

    @FXML
    private void wstawianieDanychNauczycielaDoTextField() {
        tabela_nauczycieli.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //tabela_nauczycieli.getSelectionModel().clearSelection();
                Nauczyciel naucz_tabela = tabela_nauczycieli.getSelectionModel().getSelectedItem();
                e_imie_n.setText(naucz_tabela.getImie());
                e_nazwisko_n.setText(naucz_tabela.getNazwisko());
                pesel_edycja.setText(naucz_tabela.getPesel() + "");
            }
        });

    }

    @FXML
    private void edytowanieNauczycielaPrzezTabelke() {
        String imie = e_imie_n.getText();
        String nazwisko = e_nazwisko_n.getText();
        Long peselek = Long.parseLong(pesel_edycja.getText());

        try {
            edytujNauczyciela(peselek, imie, nazwisko);
            e_imie_n.setText("");
            e_nazwisko_n.setText("");
            pesel_edycja.setText("");
            label_edycja.setText("Edytowano!");
            wstawienieDoTabeli();
            ustawWartosciPeseliRodzicaEdycja();
        } catch (Exception e) {
            label_edycja.setText("Niepoprawne dane!");
        }

    }

    @FXML
    private void usunNauczycielowiDostep() {
        int indeks = box_nauczycieli.getSelectionModel().getSelectedIndex();
        if (indeks >= 0) {
            List<Long> pesele_n = pobierzListePeseliNauczycieli();
            Long pesel_naucz = pesele_n.get(indeks);
            usunAutoryzacjeNauczyciela(pesel_naucz);
            label_usuwanie.setText("Usunięto dostęp nauczycielowi!");
        }
    }
    
    @FXML
    private void czyszczenieLabelaUsuwanie(){
        label_usuwanie.setText("");
    }
    
     private void ustawWartosciPeseliRodzicaEdycja() {
        List<String> peselki = zrobListeImieINazwiskoNauczyciela();
        ObservableList<String> lista = FXCollections.observableArrayList(peselki);
        box_nauczycieli.setItems(lista);
        if (peselki.size() > 0) {
            // e_pesel_r.setValue(peselki.get(0));
        }
    }

    private List<String> zrobListeImieINazwiskoNauczyciela() {
        List<Long> peselki = pobierzListePeseliNauczycieli();
        List<String> przedstawienie = new ArrayList<String>();
        for (int i = 0; i < peselki.size(); i++) {
            Nauczyciel rodzic = zwrocNauczyciela(peselki.get(i));
            String imie = rodzic.getImie();
            String nazwisko = rodzic.getNazwisko();
            przedstawienie.add(imie + " " + nazwisko);
        }
        return przedstawienie;
    }
}
