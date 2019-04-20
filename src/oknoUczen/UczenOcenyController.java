/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoUczen;

import utilities.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import utilities.Utils;

public class UczenOcenyController implements Initializable {

    private final long PESEL = 32222222220L;
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
    @FXML
    private TableView tabelaOcen;
    @FXML
    private TableColumn kolumna1;
    @FXML
    private TableColumn kolumna2;
    @FXML
    private TableColumn kolumna3;
    @FXML
    private TableColumn kolumna4;
    @FXML
    private TableColumn kolumna5;
    @FXML
    private TableColumn kolumna6;
    @FXML
    private TableColumn kolumna7;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        zmianaNazwKolumn();
        tabelaOcen.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaOcen));
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
        tabelaOcen.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaOcen));
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

    @FXML
    public void zmianaNazwKolumn() {
        String[] nazwyKolumn = HibernateUtil.pobieranieNazwPrzedmiotow();
        ObservableList<TableColumn> kolumna = tabelaOcen.getColumns();
        
        int i = 0;

        for (TableColumn kol : kolumna) {
            kol.setText(nazwyKolumn[i]);
            i++;
        }

    }
    
}
