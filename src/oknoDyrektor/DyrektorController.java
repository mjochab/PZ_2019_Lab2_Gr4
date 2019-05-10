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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import static utilities.HibernateUtil.podajPeseleNauczycielaBezDanych;
import static utilities.HibernateUtil.uzyskajLoginZalogowany;
import static utilities.HibernateUtil.uzyskajPesel;
import static utilities.HibernateUtil.wstawAutoryzacje;
import static utilities.HibernateUtil.wstawNauczyciela;

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
    private TextField imie_n;
    @FXML
    private TextField nazwisko_n;
    @FXML
    private ChoiceBox pesel_n;

    private String username = "xd";
    private Long pesel = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ustawWartosciBox();
        ustawWartosciPeseliLoginow();
        wstawUseraDoZalogowanoJako(username);
        Platform.runLater(() -> {
            //wstawUseraDoZalogowanoJako(username);
        });
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
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor.fxml"));
        //rootPane.getChildren().setAll(pane);
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
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor_rodzic.fxml"));
        //rootPane.getChildren().setAll(pane);
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

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor_uczen.fxml"));
        //rootPane.getChildren().setAll(pane);
    }

    public void wstawUseraDoZalogowanoJako(String username) {
        userid.setText(username);
    }

    public void wstawPesel(Long nr_pesel) {
        //tajne.setText(nr_pesel.toString());
    }

    public void przekazNazweUzytkownikaIPesel(String username, Long nr_pesel) {
        this.username = username;
        pesel = nr_pesel;
    }

    public void przekazNazweUzytkownika(String username) {
        this.username = username;
    }

    private Long getPesel() {
        String login = userid.getText();
        return uzyskajPesel(login);
    }

    //usuniety label w oknie - uwaga
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

        //Long peselN = Long.parseLong(pesel_n.getSelectionModel().getSelectedItem().toString());
        // wstawNauczyciela(peselN, imie_n.getText(), nazwisko_n.getText());
    }

}
