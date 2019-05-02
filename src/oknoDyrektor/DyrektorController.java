/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoDyrektor;

import Okna.LogowanieController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Kasia
 */

public class DyrektorController implements Initializable {  
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
    
    private String username="xd";
    //private String username=przekazNazweUzytkownikaIPesel(username,pesel);
    
    private Long pesel = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //przekazNazweUzytkownikaIPesel(username,pesel);       
       wstawUseraDoZalogowanoJako(username);
       Platform.runLater(() -> {
       //przekazNazweUzytkownikaIPesel(username,pesel);     
        //wstawUseraDoZalogowanoJako(username);
        //username = userid.getText();
       });
    }
    
    @FXML
    private void logout(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
             rootPane.getChildren().setAll(pane);
    }
    @FXML
    private void LoadNauczyciel(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor.fxml"));
             rootPane.getChildren().setAll(pane);
    }
    @FXML
    private void LoadRodzic(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor_rodzic.fxml"));
             rootPane.getChildren().setAll(pane);
    }
    @FXML
    private void LoadUczen(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor_uczen.fxml"));
             rootPane.getChildren().setAll(pane);
    }
    public void wstawUseraDoZalogowanoJako(String username) {

        userid.setText(username);

    }
    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
        this.username = username;
        this.pesel = pesel;
    }
    public void przekazNazweUzytkownika(String username) {
        this.username = username;
    }
    

    
}
