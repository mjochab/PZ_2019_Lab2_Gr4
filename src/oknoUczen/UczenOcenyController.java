/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoUczen;

import utilities.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import mapping.*;
import utilities.*;

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
    private TableColumn<Integer, Number> kolumna1;
    @FXML
    private TableColumn<Integer, Number> kolumna2;
    @FXML
    private TableColumn<Integer, Number> kolumna3;
    @FXML
    private TableColumn<Integer, Number> kolumna4;
    @FXML
    private TableColumn<Integer, Number> kolumna5;
    @FXML
    private TableColumn<Integer, Number> kolumna6;
    @FXML
    private TableColumn<Integer, Number> kolumna7;

    private final long pesel = 32222222221L;
    public String[] nazwyKolumn;
    public ObservableList<TableColumn> kolumna;
    public Uczen uczen;
    public List<Integer> listaOcen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        zmianaNazwKolumn();
        tabelaOcen.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaOcen));
        wpisywanieOcen();

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
        wpisywanieOcen();

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
        nazwyKolumn = HibernateUtil.pobieranieNazwPrzedmiotow();
        kolumna = tabelaOcen.getColumns();

        if (nazwyKolumn.length != 0) {
            int i = 0;

            for (TableColumn kol : kolumna) {
                kol.setText(nazwyKolumn[i]);
                i++;
            }
        } else {

        }
    }

    public List<Integer> zwrocOcenyDlaPrzedmiotu(Set oceny, String nazwaKolumny) {
        List<Integer> lista = new ArrayList<>();
        Iterator<Ocena> it = oceny.iterator();

        while (it.hasNext()) {
            Ocena ocena = it.next();
            if (ocena.getPrzedmiot().getNazwaPrzedmiotu().equals(nazwaKolumny)) {
                lista.add(ocena.getStopien());
            } else {
                
            }
        }
        return lista;
    }

    public void wpisywanieOcen() {
        uczen = HibernateUtil.zwrocUcznia(pesel);
        Set oceny = uczen.getOcenas();
        for (int i = 0; i < oceny.size(); i++) {
            tabelaOcen.getItems().add(i);
        }
        for (TableColumn<Integer, Number> kol : kolumna) {
            listaOcen = zwrocOcenyDlaPrzedmiotu(oceny, kol.getText());
            if (listaOcen.isEmpty()) {

            } else {
                System.out.println(listaOcen.toString());
                for (int i = 0; i < listaOcen.size(); i++) {
                    System.out.println(listaOcen.get(i).toString());                   
                }
                kol.setCellValueFactory(cellData -> {
                    Integer rowIndex = cellData.getValue();
                    return new ReadOnlyIntegerWrapper(listaOcen.get(0));
                });
            }

        }
    }

}
