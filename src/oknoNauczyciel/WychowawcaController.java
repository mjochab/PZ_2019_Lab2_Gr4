/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import mapping.Obecnosc;
import mapping.Ocena;
import mapping.Uczen;
import utilities.HibernateUtil;
import static utilities.HibernateUtil.edytujOcene;
import static utilities.HibernateUtil.zwrocKlaseKtoraWychowuje;
import static utilities.HibernateUtil.zwrocNieobecnosciUcznia;
import static utilities.HibernateUtil.zwrocUczniowZklasy;
import utilities.Utils;

/**
 * FXML Controller class
 *
 * @author Veth
 */
public class WychowawcaController implements Initializable {

  @FXML
  private AnchorPane rootPane;
  @FXML
  private Label userid1;
  @FXML
  private Label userid;
  @FXML
  private Pane paneWychowankow;
  @FXML
  private Button wychowankowieBtn;
  @FXML
  private Button obecnosciBtn;
  @FXML
  private Button logoutBtn;
  @FXML
  private Label gagatek;
  @FXML
  private Pane gagatekPane;
  @FXML
  private Label jakaKlasa;

  private static String klasa = null;
  private String username = null;
  private Long pesel = null;
  List<Uczen> uczniowie = new ArrayList<>();

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    Platform.runLater(() -> {

      wstawUseraDoZalogowanoJako(username);
      wstawKlaseDoLabela(klasa);
      setUczniowie();
      //stworzZakladkiOceny();
      //stworzZakladkiZobecnosciami();
      obecnosciBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, stworzTabeleObecnosci());
      wychowankowieBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, stworzTabeleWychowankow());
      gagatek.setVisible(false);
    });
  }

  @FXML
  private void przejdzDoWychowankow(ActionEvent event) {

  }

  @FXML
  private void przejdzDoObecnosci(ActionEvent event) {
  }

  @FXML
  private void logout(ActionEvent event) {
  }

  private void wstawKlaseDoLabela(String klasa) {
    jakaKlasa.setText(klasa);
  }

  private TableColumn stworzKolumneUczniow(String tytulIgetter) {
    TableColumn kolumna = new TableColumn(tytulIgetter);
    kolumna.setMinWidth(100);
    kolumna.setCellValueFactory(
            new PropertyValueFactory<Uczen, String>(tytulIgetter));

    return kolumna;
  }

  private void wypelnijTabeleWychowankow() {
    paneWychowankow.getChildren().clear();
    TableView<Uczen> table = new TableView<>();
    
    VBox hbox = new VBox();
    hbox.setMinWidth(350);
    List<TableColumn> kolumny = new ArrayList<>();
    kolumny.add(stworzKolumneUczniow("Imie"));
    kolumny.add(stworzKolumneUczniow("Nazwisko"));
    kolumny.add(stworzKolumneUczniow("Pesel"));
    ObservableList<Uczen> data
            = FXCollections.observableArrayList(uczniowie);
    table.setItems(data);
    table.getColumns().addAll(kolumny.get(0), kolumny.get(1), kolumny.get(2));

    table.addEventHandler(MouseEvent.MOUSE_CLICKED, zwrocEventHandleraDlaWychowankow(table, hbox));
     hbox.getChildren().add(table);
   
    paneWychowankow.getChildren().add(hbox);
  }

  private void wypelnijTabeleObecnosci() {
    paneWychowankow.getChildren().clear();

    TableView<Uczen> table = new TableView<>();

    List<TableColumn> kolumny = new ArrayList<>();
    kolumny.add(stworzKolumneUczniow("Imie"));
    kolumny.add(stworzKolumneUczniow("Nazwisko"));
    kolumny.add(stworzKolumneUczniow("Pesel"));
    ObservableList<Uczen> data
            = FXCollections.observableArrayList(uczniowie);
    table.setItems(data);
    table.getColumns().addAll(kolumny.get(0), kolumny.get(1), kolumny.get(2));

    table.addEventHandler(MouseEvent.MOUSE_CLICKED, zwrocEventHandleraDlaNieobecnych(table));

    paneWychowankow.getChildren().add(table);
  }

  private EventHandler zwrocEventHandleraDlaWychowankow(TableView<Uczen> table, VBox hbox) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

        Uczen selectedItem = table.getSelectionModel().getSelectedItem();
        wygenerujMenuRaportow(selectedItem, table, hbox);

      }
    };
    return eventHandler;
  }

  private EventHandler zwrocEventHandleraDlaNieobecnych(TableView<Uczen> table) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

        Uczen selectedItem = table.getSelectionModel().getSelectedItem();
        stworzTabeleNieobecnego(selectedItem, table);

      }
    };
    return eventHandler;
  }

  private void wygenerujMenuRaportow(Uczen uczen, TableView<Uczen> staraTabela, VBox hbox) {

    gagatekPane.getChildren().clear();

    gagatekPane.setVisible(true);

    // MASZ TU OBIEKT UCZNIA PO KLIKNIECIU NA REKORD W DUZEJ TABELI!!!!
//    gagatek.setText(uczen.getImie() + " " + uczen.getNazwisko() + " " + uczen.getKlasa().getNazwaKlasy());

    Label label = new Label();
    label.setVisible(true);
    label.setText("Pobierz raport wywiadówkowy ucznia "+uczen.getImie()+" "+uczen.getNazwisko()+" w pdf");
    label.setTextFill(Color.web("#8B0000"));

 
    Button pobierzRaport = new Button("Pobierz raport");
    pobierzRaport.setOnAction(action -> {
        try {
            // pobierz raport
            Utils.tworzeniePDF(uczen);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WychowawcaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });

    VBox validationAlertBox = new VBox();
    validationAlertBox.setSpacing(15);
    validationAlertBox.setPadding(new Insets(15, 20, 5, 10));
    validationAlertBox.setAlignment(Pos.CENTER);
    validationAlertBox.getChildren().addAll(label, pobierzRaport);
    hbox.getChildren().clear();
    
     hbox.getChildren().addAll(staraTabela,validationAlertBox);

  }

  private void stworzTabeleNieobecnego(Uczen uczen, TableView<Uczen> staraTabela) {

    gagatekPane.getChildren().clear();

    gagatekPane.setVisible(true);

    gagatek.setText(uczen.getImie() + " " + uczen.getNazwisko() + " " + uczen.getKlasa().getNazwaKlasy());

    Label label = new Label();
    label.setVisible(true);
    label.setText("Usprawiedliwianie - kliknij na nieobecność by usprawiedliwić");
    label.setTextFill(Color.web("#8B0000"));
    Label label1 = new Label();
    label1.setVisible(false);
    label1.setText("Zmieniono stan obecnosci!");
    label1.setTextFill(Color.web("#8B0000"));
    TableView<Obecnosc> table = new TableView<>();
    table.setPlaceholder(new Label("Brak godzin do usprawiedliwienia."));
    ObservableList<Obecnosc> data
            = FXCollections.observableArrayList(zwrocNieobecnosciUcznia(uczen));
    table.setItems(data);

    TableColumn kolumnaOcena = new TableColumn("Wartosc");
    kolumnaOcena.setMinWidth(100);
    kolumnaOcena.setCellValueFactory(
            new PropertyValueFactory<Obecnosc, Integer>("Wartosc"));

    TableColumn kolumnaWartosc = new TableColumn("Przedmiot");
    kolumnaWartosc.setMinWidth(175);
    kolumnaWartosc.setCellValueFactory(new Callback<CellDataFeatures<Obecnosc, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Obecnosc, String> data) {
        StringProperty ocenyUczniaDoWyswietlenia = new SimpleStringProperty();
        ocenyUczniaDoWyswietlenia.setValue(data.getValue().getPrzedmiot().getNazwaPrzedmiotu());

        return ocenyUczniaDoWyswietlenia;
      }
    });

    TableColumn kolumnaData = new TableColumn("Data");
    kolumnaData.setMinWidth(100);
    kolumnaData.setCellValueFactory(
            new PropertyValueFactory<Obecnosc, Date>("Data"));
    kolumnaData.setCellFactory(column -> {
      TableCell<Obecnosc, Date> cell = new TableCell<Obecnosc, Date>() {
        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        protected void updateItem(Date item, boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setText(null);
          } else {
            setText(format.format(item));
          }
        }
      };

      return cell;
    });

    table.getColumns().addAll(kolumnaOcena, kolumnaWartosc, kolumnaData);

    table.addEventHandler(MouseEvent.MOUSE_CLICKED, usprawiedliw(table, label1));

    VBox validationAlertBox = new VBox();
    validationAlertBox.setSpacing(15);
    validationAlertBox.setPadding(new Insets(15, 20, 5, 10));
    validationAlertBox.setAlignment(Pos.CENTER);
    gagatek.setVisible(true);
    validationAlertBox.getChildren().addAll(gagatek, label, table, label1);

    gagatekPane.getChildren().addAll(validationAlertBox);

  }

  private EventHandler usprawiedliw(TableView<Obecnosc> table, Label label) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        String stan = "";
        Obecnosc selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem.getWartosc().equals("nr")) {
          stan = "usprawiedliwiony";
          selectedItem.setWartosc("u");
          HibernateUtil.usprawiedliwNieobecnosc(selectedItem);
        } else if (selectedItem.getWartosc().equals("n")) {
          stan = "usprawiedliwiony";
          selectedItem.setWartosc("u");
          HibernateUtil.usprawiedliwNieobecnosc(selectedItem);
        } else if (selectedItem.getWartosc().equals("u")) {
          stan = "nieusprawiedliwiony";
          selectedItem.setWartosc("n");
          HibernateUtil.usprawiedliwNieobecnosc(selectedItem);
        }
        
        ustawLabel(stan, label, selectedItem);
        label.setVisible(true);
        table.refresh();
      }
    };
    return eventHandler;
  }

  private void ustawLabel(String stan, Label label, Obecnosc selectedItem){
    label.setText("Zmieniono stan obecnosci z dnia: " + selectedItem.getData().toString() + " \r\n ucznia: " + selectedItem.getUczen().getImie() + " " + selectedItem.getUczen().getNazwisko()+"\r\n na: "+stan);
  }
  private void wstawUseraDoZalogowanoJako(String username) {

    userid.setText(username);

  }

  public void przekazKlaseIusername(String klasa, String username, Long pesel) {
    this.username = username;
    this.klasa = klasa;
    this.pesel = pesel;

  }

  public String getKlasa() {
    return klasa;
  }

  public void setUczniowie() {

    this.uczniowie = zwrocUczniowZklasy(zwrocKlaseKtoraWychowuje(pesel));
  }

  private EventHandler stworzTabeleObecnosci() {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

        wypelnijTabeleObecnosci();
        gagatekPane.setVisible(false);
        gagatek.setVisible(false);
      }
    };
    return eventHandler;
  }

  private EventHandler stworzTabeleWychowankow() {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

        wypelnijTabeleWychowankow();
        gagatekPane.setVisible(false);
        gagatek.setVisible(false);
      }
    };

    return eventHandler;
  }

}
