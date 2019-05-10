/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoDyrektor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import static utilities.HibernateUtil.*;

/**
 * FXML Controller class
 *
 * @author Kasia
 */
public class Dyrektor_AutoryzacjaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label userid;
    @FXML
    private TextField login_uz;
    @FXML
    private PasswordField haslo_uz;
    @FXML
    private TextField pesel_uz;
    @FXML
    private Button dodajbtn;
    @FXML
    private ChoiceBox kto_box;
    @FXML
    private Label error_label;

    private Long pesel = null;
    private String username = "uzytkownik";
    private String password = " ";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wstawUseraDoZalogowanoJako(username);
        ustawWartosciBox();
        Platform.runLater(() -> {
            //wstawNowaAutoryzacje();
        });
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
            Logger.getLogger(Dyrektor_AutoryzacjaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        DyrektorController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

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
            Logger.getLogger(Dyrektor_AutoryzacjaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_rodzicController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

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
            Logger.getLogger(Dyrektor_AutoryzacjaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Dyrektor_AutoryzacjaController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);

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
        ObservableList<String> kto_list = FXCollections.observableArrayList("n", "u", "r");
        kto_box.setItems(kto_list);
        kto_box.setValue(kto_list.get(0));
        //System.out.println(kto_box.getSelectionModel().getSelectedItem().toString());
    }

    @FXML
    private void wstawNowaAutoryzacje() {
        String login = login_uz.getText();
        String haslo = haslo_uz.getText();
        String kto = kto_box.getSelectionModel().getSelectedItem().toString();
        Long peselek = null;

        if (login.isEmpty() || haslo.isEmpty()) {
            error_label.setText("Niepoprawne dane!");
        } else if (pesel_uz.getText().isEmpty() || pesel_uz.getText().length() != 11) {
            error_label.setText("Niepoprawny pesel!");
        } else {
            try {
                peselek = Long.parseLong(pesel_uz.getText());
                if (porownaniePeseliZAutoryzacji(peselek)) {
                    error_label.setText("Taki pesel już jest w bazie!");
                } else {
                    wstawAutoryzacje(peselek, login, haslo, kto);
                    login_uz.setText("");
                    haslo_uz.setText("");
                    pesel_uz.setText("");
                    error_label.setText("Dodano do bazy!");
                }

            } catch (Exception e) {
                error_label.setText("Niepoprawny pesel!");
            }
            //wstawAutoryzacje(peselek, login, haslo, kto);

        }
    }

    private boolean porownaniePeseliZAutoryzacji(Long pesel) {
        //boolean zmienna = true;
        List<Long> peseleAut = new ArrayList<>();
        peseleAut = pobierzListePeseli();

        return peseleAut.contains(pesel);
    }

}
