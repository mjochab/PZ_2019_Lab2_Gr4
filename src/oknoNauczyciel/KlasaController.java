/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

import com.sun.javafx.logging.PlatformLogger.Level;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mapping.Ocena;
import mapping.Przedmiot;
import mapping.RodzajOceny;
import mapping.Uczen;
import org.jboss.logging.Logger;
import static utilities.HibernateUtil.*;
import static utilities.Utils.customResize;

public class KlasaController implements Initializable {

  @FXML
  private TabPane tabsPane;
  @FXML
  private Pane gagatekPane;
  @FXML
  private Label gagatek;
  @FXML
  private AnchorPane rootPane;
  @FXML
  private Label userid;

  private static String klasa = null;
  private String username = null;
  private Long pesel = null;
  List<Uczen> uczniowie = new ArrayList<>();

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

    List<Przedmiot> przedmioty = zwrocPrzedmiotyKtorychUczeDanaKlase(klasa, pesel);
    tabsPane.getTabs().clear();
    for (Przedmiot przedmiot : przedmioty) {
      tabsPane.getTabs().add(stworzZakladke(przedmiot));

    }

  }

  private Tab stworzZakladke(Przedmiot przedmiot) {
    Tab tab = new Tab();

    tab.setText(przedmiot.getNazwaPrzedmiotu());
    tab.setContent(stworzTabeleZkolumnamiOceny(przedmiot, tab));

    return tab;
  }

  private void stworzTabeleGagatka(Uczen uczen, Przedmiot przedmiot, TableView<Uczen> staraTabela, Tab tab) {
    gagatekPane.getChildren().clear();
    gagatek.setVisible(true);

    gagatek.setText(uczen.getImie() + " " + uczen.getNazwisko() + " " + uczen.getKlasa().getNazwaKlasy());

    TableView<Ocena> table = new TableView<>();
    ObservableList<Ocena> data
            = FXCollections.observableArrayList(zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu(uczen, przedmiot));
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

    table.getColumns().addAll(kolumnaOcena, kolumnaRodzaj, kolumnaData);
    customResize(table);
    VBox ustawiaczPane = new VBox();
    ustawiaczPane.setSpacing(15);
    ustawiaczPane.setPadding(new Insets(15, 20, 5, 10));
    ustawiaczPane.setAlignment(Pos.CENTER);

    TextField ocenaPole = new TextField();
    ocenaPole.setMaxWidth(25);
    TextField rodzajPole = new TextField();
    rodzajPole.setMaxWidth(40);
    TextField dataPole = new TextField();
    dataPole.setMaxWidth(80);

    HBox textfieldy = new HBox();
    textfieldy.setSpacing(15);
    textfieldy.setPadding(new Insets(15, 20, 5, 10));
    textfieldy.setAlignment(Pos.CENTER);
    textfieldy.getChildren().addAll(ocenaPole, rodzajPole, dataPole);

    Button dodajOcene = new Button("Dodaj");
    dodajOcene.setVisible(true);
    dodajOcene.addEventHandler(MouseEvent.MOUSE_CLICKED, dodajOceneButtonHandler(table, ocenaPole, rodzajPole, dataPole, uczen, przedmiot, tab));

    Button edytujOcene = new Button("Edytuj");
    edytujOcene.setVisible(false);


    Button cancel = new Button("Cofnij");
    cancel.setVisible(false);
    Event event = new Event(MouseEvent.MOUSE_CLICKED);
    Event.fireEvent(cancel, event);
    HBox buttony = new HBox();
    buttony.setSpacing(15);
    buttony.setPadding(new Insets(15, 20, 5, 10));
    buttony.setAlignment(Pos.CENTER);
    buttony.getChildren().addAll(dodajOcene, edytujOcene, cancel);

    ustawiaczPane.getChildren().addAll(table, textfieldy, buttony);

    gagatekPane.getChildren().addAll(ustawiaczPane);

    table.addEventHandler(MouseEvent.MOUSE_CLICKED, dodajButtonyWypelnijTextFieldyHandler(table, ocenaPole, rodzajPole, dataPole, dodajOcene, edytujOcene, cancel, uczen, przedmiot, tab));

    //DOROBIC CHOWANIE BUTTONOW, USUWANIE STAREJ TABELI
  }

  private TableView stworzTabeleZkolumnamiOceny(Przedmiot przedmiot, Tab tab) {
    TableView<Uczen> table = new TableView<Uczen>();
    table = wypelnijTabele(table, przedmiot);
    table.addEventHandler(MouseEvent.MOUSE_CLICKED, zwrocEventHandleraDlaRekordow(przedmiot, table, tab));
    customResize(table);
    table.setEditable(true);
    return table;

  }

  private TableColumn stworzKolumneUczniow(String tytulIgetter) {
    TableColumn kolumna = new TableColumn(tytulIgetter);
    kolumna.setMinWidth(100);
    kolumna.setCellValueFactory(
            new PropertyValueFactory<Uczen, String>(tytulIgetter));

    return kolumna;
  }

  private TableView<Uczen> wypelnijTabele(TableView<Uczen> table, Przedmiot przedmiot) {
    List<TableColumn> kolumnyzOcenami = new ArrayList<>();
    kolumnyzOcenami.add(stworzKolumneUczniow("Imie"));
    kolumnyzOcenami.add(stworzKolumneUczniow("Nazwisko"));
    ObservableList<Uczen> data
            = FXCollections.observableArrayList(zwrocUczniowZklasy(klasa));
    table.setItems(data);

    table.getColumns().addAll(kolumnyzOcenami.get(0), kolumnyzOcenami.get(1));

    List<String> rodzajeOcen = zwrocRodzajeOcen();

    int i = 2;
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
    return table;
  }

  // HANDLERY ---------------------------
  private EventHandler zwrocEventHandleraDlaRekordow(Przedmiot przedmiot, TableView<Uczen> table, Tab tab) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

        Uczen selectedItem = table.getSelectionModel().getSelectedItem();
        stworzTabeleGagatka(selectedItem, przedmiot, table, tab);

      }
    };
    return eventHandler;
  }

  private EventHandler dodajButtonyWypelnijTextFieldyHandler(TableView<Ocena> table, TextField ocena, TextField rodzaj, TextField data, Button dodaj, Button edytuj, Button cancel, Uczen uczen, Przedmiot przedmiot, Tab tab) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        Ocena selectedItem = table.getSelectionModel().getSelectedItem();
        //sprawdz null pointera
        if (!selectedItem.toString().isEmpty()) {
          ocena.setText(selectedItem.getStopien().toString());
          rodzaj.setText(selectedItem.getRodzajOceny().getRodzajOceny());
          data.setText(selectedItem.getData().toString());
          cancel.setVisible(true);
          dodaj.setVisible(false);
          edytuj.setVisible(true);
          edytuj.addEventHandler(MouseEvent.MOUSE_CLICKED, edytujOceneButtonHandler(table, ocena, rodzaj, data, uczen, przedmiot, tab, selectedItem, dodaj, cancel, edytuj ));
   //TableView<Ocena> table, TextField ocena, TextField rodzaj, TextField data, Uczen uczen, Przedmiot przedmiot, Tab tab, Ocena obiektOcenaDoEdycji, Button dodaj, Button cancel
          cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
              ocena.setText("");
              rodzaj.setText("");
              data.setText("");
              dodaj.setVisible(true);
              cancel.setVisible(false);
              edytuj.setVisible(false);
            }

          });
        }
      }
    };
    return eventHandler;
  }

  private EventHandler dodajOceneButtonHandler(TableView<Ocena> table, TextField ocena, TextField rodzaj, TextField data, Uczen uczen, Przedmiot przedmiot, Tab tab) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        Ocena obiektOcenaDoWstawienia = new Ocena();//przedmiot, rodzajOceny, Uczen uczen, Integer stopien, Date data);
        obiektOcenaDoWstawienia.setPrzedmiot(przedmiot);
        obiektOcenaDoWstawienia.setRodzajOceny(new RodzajOceny(rodzaj.getText()));
        obiektOcenaDoWstawienia.setStopien(Integer.valueOf(ocena.getText()));
        obiektOcenaDoWstawienia.setUczen(uczen);
        try {
          obiektOcenaDoWstawienia.setData(utilities.Utils.returnDate(data.getText()));
        } catch (ParseException ex) {
          java.util.logging.Logger.getLogger(KlasaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        wstawOcene(obiektOcenaDoWstawienia);
        odswiezTabele(table, przedmiot, tab, uczen);

      }
    };
    return eventHandler;
  }

  private EventHandler edytujOceneButtonHandler(TableView<Ocena> table, TextField ocena, TextField rodzaj, TextField data, Uczen uczen, Przedmiot przedmiot, Tab tab, Ocena obiektOcenaDoEdycji, Button dodaj, Button cancel, Button edytuj) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        obiektOcenaDoEdycji.setRodzajOceny(new RodzajOceny(rodzaj.getText()));
        obiektOcenaDoEdycji.setStopien(Integer.valueOf(ocena.getText()));
        try {
          obiektOcenaDoEdycji.setData(utilities.Utils.returnDate(data.getText()));
        } catch (ParseException ex) {
          java.util.logging.Logger.getLogger(KlasaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        edytujOcene(obiektOcenaDoEdycji);
        odswiezTabele(table, przedmiot, tab, uczen);
        cancel.setVisible(false);
        dodaj.setVisible(true);
        edytuj.setVisible(false);

      }
    };
    return eventHandler;
  }

  public void odswiezTabele(TableView<Ocena> tableOcena, Przedmiot przedmiot, Tab tab, Uczen uczen) {

    int ktoryTabJestWybrany = tabsPane.getSelectionModel().getSelectedIndex();
    //SingleSelectionModel wybierz = new SingleSelectionModel();
    Tab odswiezonyTab = stworzZakladke(przedmiot);
    tabsPane.getTabs().remove(ktoryTabJestWybrany);
    tabsPane.getTabs().add(odswiezonyTab);

    // wybierz nowo dodanego taba
    SingleSelectionModel<Tab> selectionModel = tabsPane.getSelectionModel();
    selectionModel.select(odswiezonyTab); //select by object

    ObservableList<Ocena> data
            = FXCollections.observableArrayList(zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu(uczen, przedmiot));
    tableOcena.setItems(data);

  }

}
