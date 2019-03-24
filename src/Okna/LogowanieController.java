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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Kasia
 */
public class LogowanieController implements Initializable {
    @FXML
    private Text login;
    @FXML
    private Text haslo;
    @FXML
    private Button zalogujbtn;
    @FXML
    private AnchorPane rootPane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    private void logIn(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor.fxml"));
             rootPane.getChildren().setAll(pane);
    }

       
    
}
