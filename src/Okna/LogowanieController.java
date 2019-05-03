/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Okna;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mapping.*;
import oknoDyrektor.DyrektorController;
import oknoNauczyciel.NauczycielKlasyController;
import oknoRodzic.RodzicController;
import oknoUczen.UczenOcenyController;
import utilities.*;
import static utilities.HibernateUtil.uzyskajKtoZalogowany;
import static utilities.HibernateUtil.uzyskajPeselZalogowany;

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
    @FXML
    private Label userid;

    private Long pesel = null;
    private String username = "uzytkownik";
    private String password = " ";

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

        if (osoba == null) {
            niepoprawne_dane.setText("Nie ma takiego użytkownika!");
        } else if (osoba.equals("n")) {
             FXMLLoader fxmlLoader = new FXMLLoader();
             fxmlLoader.setLocation(getClass().getResource("/oknoNauczyciel/NauczycielKlasy.fxml"));
            try{
               pane = fxmlLoader.load();
               rootPane.getChildren().setAll(pane);
            }
            catch(IOException ex){
               Logger.getLogger(LogowanieController.class.getName()).log(Level.SEVERE, null, ex);
            }
            NauczycielKlasyController controller = fxmlLoader.getController();
            controller.wstawUseraDoZalogowanoJako(login_field.getText());
            controller.przekazNazweUzytkownikaIPesel(login_field.getText(), pobierzPeselZalogowanego());          
            
            //pane = FXMLLoader.load(getClass().getResource("/oknoNauczyciel/NauczycielKlasy.fxml"));
            //rootPane.getChildren().setAll(pane);
        } else if (osoba.equals("d")) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/oknoDyrektor/Dyrektor.fxml"));
            try{
               pane = fxmlLoader.load();
               rootPane.getChildren().setAll(pane);
            }
            catch(IOException ex){
               Logger.getLogger(LogowanieController.class.getName()).log(Level.SEVERE, null, ex);
            }
            DyrektorController controller = fxmlLoader.getController();
            controller.wstawUseraDoZalogowanoJako(login_field.getText());
            controller.przekazNazweUzytkownikaIPesel(login_field.getText(), pobierzPeselZalogowanego());
            
            //pane = FXMLLoader.load(getClass().getResource("/oknoDyrektor/Dyrektor.fxml"));
            
        } else if (osoba.equals("u")) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/oknoUczen/UczenOceny.fxml"));
            try{
               pane = fxmlLoader.load();
               rootPane.getChildren().setAll(pane);
            }
            catch(IOException ex){
               Logger.getLogger(LogowanieController.class.getName()).log(Level.SEVERE, null, ex);
            }
            UczenOcenyController controller = fxmlLoader.getController();
            controller.wstawUseraDoZalogowanoJako(login_field.getText());
            controller.przekazNazweUzytkownikaIPesel(login_field.getText(), pobierzPeselZalogowanego());
            
            //pane = FXMLLoader.load(getClass().getResource("/oknoUczen/UczenOceny.fxml"));
            //rootPane.getChildren().setAll(pane);
        } else if (osoba.equals("r")) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/oknoRodzic/Rodzic.fxml"));
            try{
               pane = fxmlLoader.load();
               rootPane.getChildren().setAll(pane);
            }
            catch(IOException ex){
               Logger.getLogger(LogowanieController.class.getName()).log(Level.SEVERE, null, ex);
            }
            RodzicController controller = fxmlLoader.getController();
            controller.wstawUseraDoZalogowanoJako(login_field.getText());
            controller.przekazNazweUzytkownikaIPesel(login_field.getText(), pobierzPeselZalogowanego());
            
            
            //pane = FXMLLoader.load(getClass().getResource("/oknoRodzic/Rodzic.fxml"));
            //rootPane.getChildren().setAll(pane);
        } else {
            //pane = FXMLLoader.load(getClass().getResource("/okna/Logowanie.fxml"));
            System.out.println("coś nie pykło");
        }
    }

    private String pobierzKtoJestZalogowany() {
        //Long nr_pesel = 22222222225L;
        String login = login_field.getText();
        String haslo = password_field.getText();
        String ktoZal = "";
        if (!login.isEmpty() && !haslo.isEmpty()) {
            Long nr_pesel = uzyskajPeselZalogowany(login, haslo);
            ktoZal = uzyskajKtoZalogowany(nr_pesel);
        }
        if (login.isEmpty()) {
            niepoprawne_dane.setText("Proszę podać login!");
        }
        if (haslo.isEmpty()) {
            niepoprawne_dane.setText("Proszę podać haslo!");
        }
        if (login.isEmpty() && haslo.isEmpty()) {
            niepoprawne_dane.setText("Proszę podać dane logowania!");
        }

        return ktoZal;
    }

    public Long pobierzPeselZalogowanego() {
        String login = login_field.getText();      
        String haslo = password_field.getText();
        Long nr_pesel = null;
        if (!login.isEmpty() && !haslo.isEmpty()) {
            nr_pesel = uzyskajPeselZalogowany(login, haslo);
        }
        return nr_pesel;
    }

//    private void wstawUseraDoZalogowanoJako(String username) {
//        userid.setText(username);
//    }
//
//    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
//        this.username = username;
//        this.pesel = pesel;
//    }
//
//    public void przekazNazweUzytkownika(String username) {
//        this.username = username;
//    }
//
//    public void przekazNazweUzytkownikaPesel() {
//        this.username = login_field.getText();
//        this.pesel = pobierzPeselZalogowanego();
//    }


}
