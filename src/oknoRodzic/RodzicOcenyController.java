/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoRodzic;

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

/**
 * FXML Controller class
 *
 * @author Veth
 */
public class RodzicOcenyController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button uczenbtn;
    @FXML
    private Button nieobecnoscibtn;
    @FXML
    private Button kontaktbtn;
    @FXML
    private Button wylogujbtn;
 
    @FXML
    private Button usprawiedliwbtn;

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
    //ładujemy defaultowe okno z usprawiedliwieniami i nieobecnosciami ucznia
        @FXML
    private void LoadDefault(ActionEvent event) throws IOException {
          AnchorPane pane = FXMLLoader.load(getClass().getResource("Rodzic.fxml"));
          rootPane.getChildren().setAll(pane);
    }

  
}
