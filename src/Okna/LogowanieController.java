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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import mapping.*;
import utilities.*;

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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
    //tu zrobić logowanie w zależności KTO się loguje
    @FXML
    private void logowanie(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/oknoDyrektor/Dyrektor.fxml"));
             rootPane.getChildren().setAll(pane);
             
    }
    @FXML
    private void logNauczyciel(ActionEvent event) throws IOException {
        //TextField login_field = new TextField();
        // login_field.setText("elko");
         System.out.println(login_field.getText());
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

       
    
}
