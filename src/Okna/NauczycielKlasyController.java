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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;



public class NauczycielKlasyController implements Initializable {

    @FXML
    private Button klasa_1btn;
    @FXML
    private Button klasa_2btn;
    @FXML
    private Button wylogujbtn;
    @FXML
    private Text increment;
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
    private void handleButtonAction(ActionEvent event) throws IOException {

    }

    @FXML
    private void logout() {
        //todo 
    }

    //ładujemy okno z ocenami uczenia.
    @FXML
    private void LoadKlasa(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Klasa.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
