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
import javafx.scene.control.Label;
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
import mapping.Rodzic;
import mapping.Uczen;
import mapping.Zajecia;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import utilities.HibernateUtil;
import static utilities.HibernateUtil.uzyskajPesel;
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
    private TableView dzieciTB;
    @FXML
    private TableColumn<Uczen, String> imie;
    @FXML
    private TableColumn<Uczen, String> nazwisko;
    @FXML
    private TableColumn<Obecnosc, String> kolPrzedmiot;
    @FXML
    private TableColumn<Obecnosc, String> kolData;
    @FXML
    private TableColumn<Obecnosc, String> kolWartosc;
    @FXML
    private Button usprawiedliwBtn;
    @FXML
    private Label userid;

    private long pesel;
    private String username;
    ArrayList<Obecnosc> listaNieobecnosci;
    private Rodzic rodzic;
    private ObservableList<Obecnosc> data = FXCollections.observableArrayList();

    public void setRodzic(Rodzic rodzic) {
        this.rodzic = rodzic;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabelaNieob.setColumnResizePolicy((param) -> true);
        przekazNazweUzytkownikaIPesel(username, pesel);
        wstawUseraDoZalogowanoJako(username);
        Platform.runLater(() -> {
            pesel = getPesel();
            wstawianieDzieci();
            dzieciTB.addEventHandler(MouseEvent.MOUSE_CLICKED, zwrocEventHandleraDlaDzieci(dzieciTB));
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
    private void LoadNieobecnosci(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("RodzicNieobecnosci.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(RodzicNieobecnosciController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RodzicNieobecnosciController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);
        controller.setRodzic(rodzic);
    }

    @FXML
    private void LoadOceny(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Rodzic.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(RodzicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RodzicController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);
        controller.setRodzic(rodzic);
    }

    @FXML
    private void LoadUwagi(ActionEvent event) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("RodzicPlan.fxml"));
        try {
            pane = fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(RodzicPlanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RodzicPlanController controller = fxmlLoader.getController();
        controller.wstawUseraDoZalogowanoJako(username);
        controller.przekazNazweUzytkownikaIPesel(username, pesel);
        controller.setRodzic(rodzic);

    }

    private void wstawNieobecnosci(Uczen uczen) {
        tabelaNieob.getItems().clear();
        Set nieobecnosciSet = uczen.getObecnoscs();
       /*TU BUG */ listaNieobecnosci = posortujNieobecnosci(nieobecnosciSet);
        data = FXCollections.observableArrayList(nieobecnosciSet);
        tabelaNieob.setItems(data);
        addButtonToTable();
    }

    private ArrayList<Obecnosc> posortujNieobecnosci(Set nieobecnosciSet) {
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
    private void usprawiedliwNieobecnosc(ActionEvent event) throws IOException {
        Iterator<Obecnosc> it = listaNieobecnosci.iterator();
        while (it.hasNext()) {
            Obecnosc ob = it.next();
            if (ob.getWartosc().equals("nieobecny")) {
                ob.setWartosc("u");
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
                                data.setWartosc("u");
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

    private EventHandler zwrocEventHandleraDlaDzieci(TableView<Uczen> table) {

        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                Uczen selectedItem = table.getSelectionModel().getSelectedItem();
                if (tabelaNieob.getColumns().size() > 3) {
                    tabelaNieob.getColumns().remove(3);
                }
                wstawNieobecnosci(selectedItem);
            }
        };
        return eventHandler;
    }

    private void wstawianieDzieci() {
        Set dzieci = rodzic.getUczens();
        imie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

        ObservableList<Uczen> dane = FXCollections.observableArrayList(dzieci);
        dzieciTB.setItems(dane);

        kolData.setCellValueFactory(new PropertyValueFactory<>("data"));
        kolWartosc.setCellValueFactory(new PropertyValueFactory<>("wartosc"));
        kolPrzedmiot.setCellValueFactory(new Callback<CellDataFeatures<Obecnosc, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Obecnosc, String> p) {
                StringProperty nazwaPrzedmiotu = new SimpleStringProperty();
                nazwaPrzedmiotu.setValue(p.getValue().getPrzedmiot().getNazwaPrzedmiotu());
                return nazwaPrzedmiotu;
            }
        });
    }

    public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {
        this.username = username;
        this.pesel = pesel;
    }

    public void wstawUseraDoZalogowanoJako(String username) {
        userid.setText(username);
    }

    private Long getPesel() {
        String login = userid.getText();
        return uzyskajPesel(login);
    }
}
