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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private Pane panelButtonow;
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wybierzKlaseButtony();
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
    // do skonczenia


    
    
    
    
    private void wybierzKlaseButtony(){
      
      
      String [] nazwyKlas = utilities.HibernateUtil.zwrocKlasyKtoreUcze(pesel);   
      Button[] listaButtonow = new Button[nazwyKlas.length];
        for (int i = 0; i < nazwyKlas.length; i++) {
          Button b = new Button(nazwyKlas[i]);
          b.setId("klasax"+i);
          b.setText(nazwyKlas[i]);
          b.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                (event -> b.setText("kulke"))); 
          listaButtonow[i]=b;
        }
        
        panelButtonow.getChildren().clear();
        panelButtonow.getChildren().addAll(listaButtonow);
        
    
}
    
    
    
    
    
    private void wstawNazweUzytkownika(){
        
        
    }
}
