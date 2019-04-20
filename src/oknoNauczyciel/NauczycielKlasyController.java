/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import utilities.HibernateUtil;



public class NauczycielKlasyController implements Initializable {

    // do zrobienia dynamicznie
    //Benton Cos tam
    static final Long pesel=22222222220L;
    
    @FXML
    private Button klasa1;
    @FXML
    private Button klasa2;
    @FXML
    private Button wylogujbtn;
    @FXML
    private Text userid;
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        zmienNazwyButtonow();
        // TODO
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
    private void LoadKlasa(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Klasa.fxml"));
        rootPane.getChildren().setAll(pane);
    }
    
    private void zmienNazwyButtonow(){
        
        //prostacko:
        List<String> zwrocKlasy2 = HibernateUtil.zwrocKlasy();
        klasa1.setText(zwrocKlasy2.get(0));
        klasa2.setText(zwrocKlasy2.get(1));
        //zmyślnie i domyślnie:
        //rootPane.getChildren()
    
}
}
