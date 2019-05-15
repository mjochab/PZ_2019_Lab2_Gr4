/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoDyrektor;

import Okna.LogowanieController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import static utilities.HibernateUtil.uzyskajPesel;
//import static utilities.HibernateUtil.wstawNauczyciela;

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
    private TextField imie_n;
    @FXML
    private TextField nazwisko_n;
    @FXML
    private ChoiceBox pesel_n;

    private String username = "xd";
    private Long pesel = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ustawWartosciBox();
        wstawUseraDoZalogowanoJako(username);
        Platform.runLater(() -> {
            System.out.println(getPesel());
            //pesel = getPesel();
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
    // wyłapać null jeśli brak rekordów
    private void ustawWartosciBox() {
        //ChoiceBox cb = new ChoiceBox();
        List<Long> peselki = podajPeseleNauczycielaBezDanych();
        ObservableList<Long> lista = FXCollections.observableArrayList(peselki);
        pesel_n.setItems(lista);
        pesel_n.setValue(peselki.get(0));
        //System.out.println(kto_box.getSelectionModel().getSelectedItem().toString());
    }

    @FXML
    private void wstawNowegoNauczyciela() {
        System.out.println(pesel_n.getSelectionModel().getSelectedItem().toString() + " " + imie_n.getText() + " " + nazwisko_n.getText());
        //String kto_wpisany = kto_box.getSelectionModel().getSelectedItem().toString();
        //Long peselek = Long.parseLong(pesel_uz.getText());
        //wstawNauczyciela(peselek,login_uz.getText(),haslo_uz.getText(),kto_box.getSelectionModel().getSelectedItem().toString());
        Long peselN = Long.parseLong(pesel_n.getSelectionModel().getSelectedItem().toString());
      //  wstawNauczyciela(peselN,imie_n.getText(),nazwisko_n.getText());
    }
    

}
