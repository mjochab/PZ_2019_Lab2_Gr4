/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoDyrektor;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mapping.Klasa;
import mapping.Uczen;
import static utilities.HibernateUtil.pobierzKlasy;
import static utilities.HibernateUtil.podajPeseleRodzicaBezDanych;
import static utilities.HibernateUtil.podajPeseleUczniaBezDanych;
import static utilities.HibernateUtil.podajPeseleUczniaBezRodzica;
import static utilities.HibernateUtil.wstawRodzica;
import static utilities.HibernateUtil.wstawUcznia;
import static utilities.HibernateUtil.zwrocAutoryzacje;

/**
 * FXML Controller class
 *
 * @author Kasia
 */
public class Dyrektor_rodzicController implements Initializable {

    /**
     * Initializes the controller class.
     */
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
    @FXML
    private TextField imie_u;
    @FXML
    private TextField nazwisko_u;
    @FXML
    private TextField imie_m;
    @FXML
    private TextField imie_o;
    @FXML
    private TextField nazwisko_m;
    @FXML
    private TextField nazwisko_o;
    @FXML
    private ChoiceBox pesel_u;
    @FXML
    private ChoiceBox pesel_r;
    @FXML
    private ChoiceBox pesel_dz;
    @FXML
    private ChoiceBox klasa;

    private Long pesel = null;
    private String username = "rodzic";
    private String password = " ";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println(pesel);
        ustawWartosciBox();
        wstawUseraDoZalogowanoJako(username);
        // TODO
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadNauczyciel(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dyrektor.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(Dyrektor_rodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        DyrektorController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor.fxml"));
        //rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadRodzic(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dyrektor_rodzic.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(Dyrektor_rodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_rodzicController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor_rodzic.fxml"));
        //rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUczen(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dyrektor_Autoryzacja.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(Dyrektor_rodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_AutoryzacjaController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

        //AnchorPane pane = FXMLLoader.load(getClass().getResource("Dyrektor_uczen.fxml"));
        //rootPane.getChildren().setAll(pane);
    }

    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
        this.username = username;
        this.pesel = pesel;
    }

    public void przekazNazweUzytkownika(String username) {
        this.username = username;
    }

    public void wstawUseraDoZalogowanoJako(String username) {
        userid.setText(username);

    }
    private void ustawWartosciBox() {
        //ChoiceBox cb = new ChoiceBox();
        List<Long> peselki_r = podajPeseleRodzicaBezDanych();
        ObservableList<Long> lista_r = FXCollections.observableArrayList(peselki_r);
        pesel_r.setItems(lista_r);
        if(peselki_r.size()>0){
            pesel_r.setValue(peselki_r.get(0));
        }
        
        List<Long> peselki_u = podajPeseleUczniaBezDanych();
        ObservableList<Long> lista_u = FXCollections.observableArrayList(peselki_u);
        pesel_u.setItems(lista_u);
          if(peselki_u.size()>0){
            pesel_u.setValue(peselki_u.get(0));
          }
        
        List<Long> peselki_dz = podajPeseleUczniaBezRodzica();
        ObservableList<Long> lista_dz = FXCollections.observableArrayList(peselki_dz);
        pesel_dz.setItems(lista_dz);
          if(peselki_dz.size()>0){
            pesel_dz.setValue(peselki_dz.get(0));
          }
        
        ObservableList<String> nazwy_k = FXCollections.observableArrayList("1a", "1b", "1c");
        klasa.setItems(nazwy_k);
        klasa.setValue(nazwy_k.get(0));
        
        //System.out.println(kto_box.getSelectionModel().getSelectedItem().toString());
    }
    
    //koniecznie parsowanie w razie null
    @FXML
    private void wstawNowegoUcznia() {
        Long peselU = Long.parseLong(pesel_u.getSelectionModel().getSelectedItem().toString());
        Klasa klasa_U = new Klasa(klasa.getSelectionModel().getSelectedItem().toString());
        wstawUcznia(peselU,imie_u.getText(),nazwisko_u.getText(), klasa_U);
    }
    
    @FXML
    private void wstawNowegoRodzica() {
        Long peselU = Long.parseLong(pesel_dz.getSelectionModel().getSelectedItem().toString());
        Long peselR = Long.parseLong(pesel_r.getSelectionModel().getSelectedItem().toString());
        Uczen uczenU = new Uczen(zwrocAutoryzacje(peselU));
        
        wstawRodzica(peselR, peselU,imie_o.getText(), nazwisko_o.getText(), imie_m.getText(), nazwisko_m.getText());
    }
}
