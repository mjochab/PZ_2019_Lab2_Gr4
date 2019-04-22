/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import mapping.Uczen;
import static utilities.HibernateUtil.zwrocUczniowZklasy;

public class KlasaController implements Initializable {

    @FXML
    private Button dodaj_ocenebtn;
    @FXML
    private Button dodaj_nieobecnoscbtn;
    @FXML
    private Button usprawiedliwbtn;
    @FXML
    private Text increment;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button powrotbtn;
    @FXML
    private Label userid;
    private String klasa = null;
    private String username = null;
    List<Uczen> uczniowie = new ArrayList<>();

    /**
     * platform Run later zeby przekazac zmienne poprawnie
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {    Platform.runLater(() -> {

        //do stuff// dziala niestety po inicjalizacji, wiec nie ustawi username :(
         wstawUseraDoZalogowanoJako(username);
    });
        
         
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
    private void LoadPowrot(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("NauczycielKlasy.fxml"));
        rootPane.getChildren().setAll(pane);
        System.out.println(username);
    }

    @FXML
    private void LoadDodajOcene(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("DodajOcene.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadDodajNieobecnosc(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("DodajNieobecnosc.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUsprawiedliw(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("DodajUsprawiedliwienie.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    private void wstawUseraDoZalogowanoJako(String username) {

        userid.setText(username);

    }

    public void przekazKlaseIusername(String klasa, String username) {
        this.username = username;
        this.klasa = klasa;
        this.uczniowie=zwrocUczniowZklasy(klasa);

    }

    public String getKlasa() {
        return klasa;
    }

}
