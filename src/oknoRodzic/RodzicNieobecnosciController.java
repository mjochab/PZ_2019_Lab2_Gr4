/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoRodzic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mapping.Obecnosc;
import mapping.Ocena;
import mapping.Przedmiot;
import mapping.Uczen;
import mapping.Zajecia;
import utilities.HibernateUtil;
import utilities.Utils;

public class RodzicNieobecnosciController implements Initializable {

    @FXML
    private Button ocenybtn;
    @FXML
    private Button nieobecnoscibtn;
    @FXML
    private Button uwagibtn;
    @FXML
    private Button wylogujbtn;
    @FXML
    private Text increment;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView tabelaNieob;
    @FXML
    private TableColumn<Obecnosc, String> kolPrzedmiot;
    @FXML
    private TableColumn<Obecnosc, String> kolData;
    @FXML
    private TableColumn<Obecnosc, String> kolWartosc;
    @FXML
    private Button usprawiedliwBtn;

    private final long PESEL = 32222222221L;
    ArrayList<Obecnosc> listaNieobecnosci;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wstawNieobecnosci();
        tabelaNieob.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaNieob));
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
    private void LoadNieobecnosci(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("RodzicNieobecnosci.fxml"));
        rootPane.getChildren().setAll(pane);
        tabelaNieob.setColumnResizePolicy((param) -> true);
        Platform.runLater(() -> Utils.customResize(tabelaNieob));
        wstawNieobecnosci();
    }

    @FXML
    private void LoadOceny(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Rodzic.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void LoadUwagi(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("RodzicPlan.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void wstawNieobecnosci() {
        Uczen uczen = HibernateUtil.zwrocUcznia(PESEL);
        kolData.setCellValueFactory(new PropertyValueFactory<>("data"));
        kolWartosc.setCellValueFactory(new PropertyValueFactory<>("wartosc"));
        kolPrzedmiot.setCellValueFactory(new Callback<CellDataFeatures<Obecnosc, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Obecnosc, String> p) {
                StringProperty nazwaPrzedmiotu = new SimpleStringProperty();
                nazwaPrzedmiotu.setValue(p.getValue().getPrzedmiot().getNazwaPrzedmiotu());
                return nazwaPrzedmiotu;
            }
        });
        Set nieobecnosciSet = uczen.getObecnoscs();
        listaNieobecnosci = posortujNieobecnosci(nieobecnosciSet);

        ObservableList<Obecnosc> dane = FXCollections.observableArrayList(listaNieobecnosci);
        tabelaNieob.setItems(dane);
        addButtonToTable();
    }

    public ArrayList<Obecnosc> posortujNieobecnosci(Set nieobecnosciSet) {
        ArrayList<Obecnosc> obecnosci = new ArrayList<Obecnosc>();
        Iterator<Obecnosc> it = nieobecnosciSet.iterator();

        while (it.hasNext()) {
            Obecnosc ob = it.next();
            if (ob.getWartosc().equals("n")) {
                ob.setWartosc("nieobecny");
                obecnosci.add(ob);
            } else if (ob.getWartosc().equals("nr")) {
                ob.setWartosc("oczekujace");
                obecnosci.add(ob);
            } else if (ob.getWartosc().equals("u")) {
                ob.setWartosc("usprawiedliwione");
                obecnosci.add(ob);
            }
        }
        Comparator<Obecnosc> porownajPoGodzinie = (Obecnosc o1, Obecnosc o2)
                -> o1.getData().compareTo(o2.getData());
        Collections.sort(obecnosci, porownajPoGodzinie);

        return obecnosci;
    }

    @FXML
    public void usprawiedliwNieobecnosc(ActionEvent event) throws IOException {
        Iterator<Obecnosc> it = listaNieobecnosci.iterator();
        while (it.hasNext()) {
            Obecnosc ob = it.next();
            if (ob.getWartosc().equals("nieobecny")) {
                ob.setWartosc("oczekujace");
                HibernateUtil.edytujNieobecnosc(ob);
                tabelaNieob.refresh();
            }
        }
    }

    private void addButtonToTable() {
        TableColumn<Obecnosc, Obecnosc> colBtn = new TableColumn("");

        Callback<TableColumn<Obecnosc, Obecnosc>, TableCell<Obecnosc, Obecnosc>> cellFactory = new Callback<TableColumn<Obecnosc, Obecnosc>, TableCell<Obecnosc, Obecnosc>>() {
            @Override
            public TableCell<Obecnosc, Obecnosc> call(final TableColumn<Obecnosc, Obecnosc> param) {
                final TableCell<Obecnosc, Obecnosc> cell = new TableCell<Obecnosc, Obecnosc>() {

                    private final Button btn = new Button("Usprawiedliw");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Obecnosc data = getTableView().getItems().get(getIndex());
                            if (data.getWartosc().equals("nieobecny")) {
                                data.setWartosc("oczekujace");
                                HibernateUtil.edytujNieobecnosc(data);
                                tabelaNieob.refresh();
                                btn.setDisable(true);
                            } else {
                                btn.setDisable(true);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Obecnosc item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            if (!item.getWartosc().equals("nieobecny")) {
                                btn.setDisable(true);
                                setGraphic(btn);
                            } else {
                                setGraphic(btn);
                            }
                        }
                    }

                };

                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        colBtn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<Obecnosc>(data.getValue()));
        tabelaNieob.getColumns().add(colBtn);
    }
}
