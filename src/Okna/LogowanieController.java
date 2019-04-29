/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Okna;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import mapping.*;
import utilities.*;
import static utilities.HibernateUtil.uzyskajKtoZalogowany;
import static utilities.HibernateUtil.uzyskajPeselZalogowany;

/**
 * FXML Controller class
 *
 * @author Kasia
 */
public class LogowanieController implements Initializable {
    @FXML
    private TextField login_field;
    @FXML
    private TextField password_field;
    @FXML
    private Button zalogujbtn;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label niepoprawne_dane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
   
    @FXML
    private void logDyrek(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/oknoDyrektor/Dyrektor.fxml"));
        rootPane.getChildren().setAll(pane);      
    }
    
    @FXML
    private void logNauczyciel(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/oknoNauczyciel/NauczycielKlasy.fxml"));
        rootPane.getChildren().setAll(pane);
    }
    @FXML
    private void logRodzic(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/oknoRodzic/Rodzic.fxml"));
             rootPane.getChildren().setAll(pane);
    }
    @FXML
    private void logUczen(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/oknoUczen/UczenOceny.fxml"));
             rootPane.getChildren().setAll(pane);
    }
    
    @FXML
    private void logowanie(ActionEvent event) throws IOException {
        AnchorPane pane;
        String osoba = pobierzKtoJestZalogowany();
        if(osoba==null){
             niepoprawne_dane.setText("Nie ma takiego użytkownika!");
        }else if (osoba.equals("n")){
            pane = FXMLLoader.load(getClass().getResource("/oknoNauczyciel/NauczycielKlasy.fxml"));
            rootPane.getChildren().setAll(pane);
        }else if (osoba.equals("d")) {
            pane = FXMLLoader.load(getClass().getResource("/oknoDyrektor/Dyrektor.fxml"));
            rootPane.getChildren().setAll(pane);
        }
        else if (osoba.equals("u")) {
            pane = FXMLLoader.load(getClass().getResource("/oknoUczen/UczenOceny.fxml"));
            rootPane.getChildren().setAll(pane);
        }
        else if (osoba.equals("r")) {
            pane = FXMLLoader.load(getClass().getResource("/oknoRodzic/Rodzic.fxml"));
            rootPane.getChildren().setAll(pane);
        }
        else {
             //pane = FXMLLoader.load(getClass().getResource("/okna/Logowanie.fxml"));
             System.out.println("coś nie pykło");
        }
    }
  
    private String pobierzKtoJestZalogowany(){
        //Long nr_pesel = 22222222225L;
        String login = login_field.getText();
        String haslo = password_field.getText();
        String ktoZal="";
        if(!login.isEmpty() && !haslo.isEmpty()){
            Long nr_pesel = uzyskajPeselZalogowany(login,haslo);   
            ktoZal = uzyskajKtoZalogowany(nr_pesel);
        }
        if(login.isEmpty()){
            niepoprawne_dane.setText("Proszę podać login!");
        }
        if(haslo.isEmpty()){
            niepoprawne_dane.setText("Proszę podać haslo!");
        }
        if(login.isEmpty() && haslo.isEmpty()){
            niepoprawne_dane.setText("Proszę podać dane logowania!");
        }
        
        return ktoZal;
    }
   
}
