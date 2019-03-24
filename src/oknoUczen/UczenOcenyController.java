/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoUczen;

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


public class UczenOcenyController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button ocenybtn;
    @FXML
    private Button nieobecnoscibtn;
    @FXML
    private Button uwagibtn;
    @FXML
    private Button wylogujbtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
             rootPane.getChildren().setAll(pane);
    }

    //ładujemy defaultowe okno z ocenami ucznia
    @FXML
    private void LoadOceny(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenOceny.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadNieobecnosci(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenNieobecnosci.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUwagi(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("UczenUwagi.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
