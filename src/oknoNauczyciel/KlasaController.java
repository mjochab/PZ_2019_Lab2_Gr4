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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import mapping.Uczen;
import static utilities.HibernateUtil.zwrocUczniowZklasy;
import static utilities.Utils.customResize;

public class KlasaController implements Initializable {

    @FXML
    private Button dodaj_ocenebtn;
    @FXML
    private Button dodaj_nieobecnoscbtn;
    @FXML
    private Button usprawiedliwbtn;
    @FXML
    private AnchorPane tabPane;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button powrotbtn;
    @FXML
    private Label userid;
    private String klasa = null;
    private String username = null;
    // do zrobienia po wybraniu taba:
    private String przedmiot = null;
    List<Uczen> uczniowie = new ArrayList<>();

    /**
     * platform Run later zeby przekazac zmienne poprawnie
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {    Platform.runLater(() -> {

        
         wstawUseraDoZalogowanoJako(username);
         setUczniowie();
         stworzTabeleZocenami("gowno");
         
    });
        
         
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
    private void LoadPowrot(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("NauczycielKlasy.fxml"));
        rootPane.getChildren().setAll(pane);
        System.out.println(username);
    }

    @FXML
    private void LoadDodajOcene(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("DodajOcene.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadDodajNieobecnosc(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("DodajNieobecnosc.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUsprawiedliw(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("DodajUsprawiedliwienie.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    private void wstawUseraDoZalogowanoJako(String username) {

        userid.setText(username);

    }

    public void przekazKlaseIusername(String klasa, String username) {
        this.username = username;
        this.klasa = klasa;
        

    }

    public String getKlasa() {
        return klasa;
    }

    public void setUczniowie() {
        this.uczniowie = zwrocUczniowZklasy(klasa);
    }
    
    
   private void stworzTabeleZocenami(String przedmiot){
       
        TableView<Uczen> table = new TableView<Uczen>();
        table.setEditable(true);
 
        TableColumn firstNameCol = new TableColumn("Imie");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Uczen, String>("imie"));
 
        TableColumn lastNameCol = new TableColumn("Nazwisko");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Uczen, String>("nazwisko"));
 
//        TableColumn emailCol = new TableColumn("Email");
//        emailCol.setMinWidth(200);
//        emailCol.setCellValueFactory(
//                new PropertyValueFactory<Person, String>("email"));
        ObservableList<Uczen> data =
        FXCollections.observableArrayList(uczniowie);
        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol);
        
        tabPane.getChildren().clear();
        customResize(table);
        tabPane.getChildren().add(table);
        
        
    }
}
