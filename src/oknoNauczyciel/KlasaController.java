/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mapping.Ocena;
import mapping.Przedmiot;
import mapping.RodzajOceny;
import mapping.Uczen;
import static utilities.HibernateUtil.*;
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
  private TabPane tabsPane;
  @FXML
  private Pane gagatekPane;
  @FXML
  private Label gagatek;
  @FXML
  private AnchorPane rootPane;
  @FXML
  private Button powrotbtn;
  @FXML
  private Label userid;
  private static String klasa = null;
  private String username = null;
  // do zrobienia po wybraniu taba:
  private String przedmiot = null;
  private Long pesel = null;
  List<Uczen> uczniowie = new ArrayList<>();

  /**
   * platform Run later zeby przekazac zmienne poprawnie
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    Platform.runLater(() -> {

      wstawUseraDoZalogowanoJako(username);
      setUczniowie();
      //stworzTabeleZocenami("gowno");
      stworzZakladki();

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

  public void przekazKlaseIusername(String klasa, String username, Long pesel) {
    this.username = username;
    this.klasa = klasa;
    this.pesel = pesel;

  }

  public String getKlasa() {
    return klasa;
  }

  public void setUczniowie() {
    this.uczniowie = zwrocUczniowZklasy(klasa);
  }

  private void stworzZakladki() {
    // to do zakladki
    // https://stackoverflow.com/questions/30656895/javafx-tabbed-pane-with-a-table-view-on-each-tab
    // buttony
    // https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view

    List<Przedmiot> przedmioty = zwrocPrzedmiotyKtorychUczeDanaKlase(klasa, pesel);
    for (Przedmiot przedmiot : przedmioty) {
      Tab tabA = new Tab();
      tabA.setText(przedmiot.getNazwaPrzedmiotu());
      tabA.setContent(stworzTabeleZkolumnamiOceny(przedmiot));
      tabsPane.getTabs().removeAll();
      tabsPane.getTabs().add(tabA);

    }

  }

  // funkcja do testow
  private void stworzTabeleGagatka(Uczen uczen, Przedmiot przedmiot) {
    gagatek.setVisible(true);

    gagatek.setText(uczen.getImie() + " " + uczen.getNazwisko() + " " + uczen.getKlasa().getNazwaKlasy());

    List<Ocena> ocenyGagatka = zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu(uczen, przedmiot);
    // kolumny z Ocena
    TableView<Ocena> table = new TableView<>();
    for (Ocena ocenka : ocenyGagatka) {
      System.out.println(ocenka.getData().toString());
    }
    ObservableList<Ocena> data
            = FXCollections.observableArrayList(ocenyGagatka);
    table.setItems(data);

    TableColumn kolumnaOcena = new TableColumn("Ocena");
    kolumnaOcena.setMinWidth(100);
    kolumnaOcena.setCellValueFactory(
            new PropertyValueFactory<Ocena, Integer>("Stopien"));

    TableColumn kolumnaRodzaj = new TableColumn("Rodzaj");
    kolumnaRodzaj.setMinWidth(100);
    kolumnaRodzaj.setCellValueFactory(new Callback<CellDataFeatures<Ocena, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Ocena, String> data) {
        StringProperty ocenyUczniaDoWyswietlenia = new SimpleStringProperty();
        ocenyUczniaDoWyswietlenia.setValue(data.getValue().getRodzajOceny().getRodzajOceny());

        return ocenyUczniaDoWyswietlenia;
      }
    });

    TableColumn kolumnaData = new TableColumn("Data");
    kolumnaData.setMinWidth(100);
    kolumnaData.setCellValueFactory(
            new PropertyValueFactory<Ocena, Date>("Data"));
    kolumnaData.setCellFactory(column -> {
      TableCell<Ocena, Date> cell = new TableCell<Ocena, Date>() {
        private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

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

    table.getColumns().addAll(kolumnaOcena, kolumnaRodzaj, kolumnaData);
    customResize(table);
    gagatekPane.getChildren().addAll(table);
    
    
    // do dodania tu buttony, add/edit/cancel
    // listenery biektu ocena zeby od razu wartosci podmienic w textviewach, zedytowac, wyslac do bazy, zrefreshowac tabele glowna.
    
    

  }

  private TableColumn stworzKolumneUczniow(String tytulIgetter) {
    TableColumn kolumna = new TableColumn(tytulIgetter);
    kolumna.setMinWidth(100);
    kolumna.setCellValueFactory(
            new PropertyValueFactory<Uczen, String>(tytulIgetter));

    return kolumna;
  }

  private TableView stworzTabeleZkolumnamiOceny(Przedmiot przedmiot) {

    //5 kolumn - sprawdzian, kartkowka, odpowiedz, referat, zadanie domowe
    TableView<Uczen> table = new TableView<Uczen>();

    ObservableList<Uczen> data
            = FXCollections.observableArrayList(uczniowie);
    table.setItems(data);
    table.getColumns().addAll(stworzKolumneUczniow("Imie"), stworzKolumneUczniow("Nazwisko"));

    List<String> rodzajeOcen = zwrocRodzajeOcen();
    List<TableColumn> kolumnyzOcenami = new ArrayList<>();
    int i = 0;
    // tworz tyle kolumn ile rodzajow ocen
    for (String rodzajOceny : rodzajeOcen) {
      kolumnyzOcenami.add(new TableColumn(rodzajOceny));
      // wpisywanie ocen do odpowiednich kolumn
      kolumnyzOcenami.get(i).setCellValueFactory(new Callback<CellDataFeatures<Uczen, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Uczen, String> data) {
          StringProperty ocenyUczniaDoWyswietlenia = new SimpleStringProperty();
          // oceny uczniow danej klasy z danego przedmiotu;
          Set ocenyUczniowKtorychUcze = data.getValue().getOcenas();
          String ocenydoWyswietlenia = "";
          for (Iterator iterator = ocenyUczniowKtorychUcze.iterator(); iterator.hasNext();) {
            Ocena ocena = (Ocena) iterator.next();

            if (ocena.getRodzajOceny().getRodzajOceny().equals(rodzajOceny) && ocena.getPrzedmiot().getNazwaPrzedmiotu().equals(przedmiot.getNazwaPrzedmiotu())) {
              ocenydoWyswietlenia = ocenydoWyswietlenia + ocena.getStopien() + ", ";
            }
          }

          ocenyUczniaDoWyswietlenia.setValue(ocenydoWyswietlenia);
          return ocenyUczniaDoWyswietlenia;
        }
      });

      table.getColumns().add(kolumnyzOcenami.get(i));
      i++;

    }

    table.addEventHandler(MouseEvent.MOUSE_CLICKED, zwrocEventHandleraDlaRekordow(przedmiot, table));

    customResize(table);
    table.setEditable(true);
    return table;

  }

  private EventHandler zwrocEventHandleraDlaRekordow(Przedmiot przedmiot, TableView<Uczen> table) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        Uczen selectedItem = table.getSelectionModel().getSelectedItem();
        stworzTabeleGagatka(selectedItem, przedmiot);

      }
    };
    return eventHandler;
  }
}
