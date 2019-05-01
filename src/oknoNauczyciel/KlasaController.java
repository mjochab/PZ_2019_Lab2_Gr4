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
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
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
import javafx.util.StringConverter;
import mapping.Obecnosc;
import mapping.Ocena;
import mapping.Przedmiot;
import mapping.RodzajOceny;
import mapping.Uczen;
import org.jboss.logging.Logger;
import utilities.HibernateUtil;
import static utilities.HibernateUtil.*;
import static utilities.Utils.customResize;
import static utilities.Utils.zwrocDatyWktorychMamZajecia;

public class KlasaController implements Initializable {

  @FXML
  private TabPane tabsPane;
  @FXML
  private Pane gagatekPane;
  @FXML
  private Label gagatek;
  @FXML
  private Label jakaKlasa;
  @FXML
  private AnchorPane rootPane;
  @FXML
  private Label userid;
  @FXML
  private Button przejdzDoOcen;
  @FXML
  private Button przejdzDoObecnosci;

  private static String klasa = null;
  private String username = null;
  private Long pesel = null;
  List<Uczen> uczniowie = new ArrayList<>();

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    Platform.runLater(() -> {

      wstawUseraDoZalogowanoJako(username);
      wstawKlaseDoLabela(klasa);
      setUczniowie();
      //stworzZakladkiOceny();
      //stworzZakladkiZobecnosciami();
      przejdzDoObecnosci.addEventHandler(MouseEvent.MOUSE_CLICKED, stworzTabeleObecnosci());
      przejdzDoOcen.addEventHandler(MouseEvent.MOUSE_CLICKED, stworzTabeleOceny());
    });

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

  // OBECNOSCI
  private void stworzZakladkiZobecnosciami() {

    tabsPane.getTabs().clear();
    List<Przedmiot> przedmioty = zwrocPrzedmiotyKtorychUczeDanaKlase(klasa, pesel);

    for (Przedmiot przedmiot : przedmioty) {

      ObservableList<Uczen> dataDoTabeli
              = FXCollections.observableArrayList(zwrocUczniowZklasy(klasa));
      Tab przedmiotyTab = new Tab(przedmiot.getNazwaPrzedmiotu());

      // SEMESTR 1
      Tab semestr1 = new Tab("Półrocze 1");
      TabPane semestr1Pane = new TabPane();

      semestr1Pane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
      for (int i = 9; i <= 12; i++) {
        Tab miesiac = new Tab(String.valueOf(i));
        miesiac.setContent(stworzTabeleZobecnosciami(przedmiot, i, dataDoTabeli, przedmiotyTab));
        semestr1Pane.getTabs().add(miesiac);

      }

      for (int i = 1; i < 2; i++) {
        Tab miesiac = new Tab(String.valueOf(i));
        miesiac.setContent(stworzTabeleZobecnosciami(przedmiot, i, dataDoTabeli, przedmiotyTab));
        semestr1Pane.getTabs().add(miesiac);

      }

      semestr1.setContent(semestr1Pane);

      // SEMESTR 2
      Tab semestr2 = new Tab("Półrocze 2");

      TabPane semestr2Pane = new TabPane();
      semestr2Pane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

      for (int i = 2; i <= 6; i++) {
        Tab miesiac = new Tab(String.valueOf(i));
        miesiac.setContent(stworzTabeleZobecnosciami(przedmiot, i, dataDoTabeli, przedmiotyTab));
        semestr2Pane.getTabs().add(miesiac);

      }

      semestr2.setContent(semestr2Pane);

      TabPane nowyTabPane = new TabPane(semestr1, semestr2);
      nowyTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
      przedmiotyTab.setContent(nowyTabPane);

      tabsPane.getTabs().add(przedmiotyTab);
    }
  }

  private TableView stworzTabeleZobecnosciami(Przedmiot przedmiot, int i, ObservableList<Uczen> data, Tab tab) {
    TableView<Uczen> table = new TableView<>();
    TableColumn kolumnaImie = stworzKolumneUczniow("Imie");
    TableColumn kolumnaNazwisko = stworzKolumneUczniow("Nazwisko");

    table.setItems(data);

    table.getColumns().addAll(kolumnaImie, kolumnaNazwisko);

    int year = 2019;
    if (i < 9) {
      year = 2020;
    }
    final int rok = year;
    List<Integer> dniTygodniaZajecia = zwrocWJakieDniTygodniaMamZajecia(pesel, przedmiot);
    List<Integer> dniMiesiacaZajecia = zwrocDatyWktorychMamZajecia(year, i, dniTygodniaZajecia);

    for (Integer zajeciaWmiesiacu : dniMiesiacaZajecia) {

      //dany dzien
      Date dataWkomorce = null;
      try {
        dataWkomorce = utilities.Utils.returnDate(rok + "-" + i + "-" + zajeciaWmiesiacu);
      } catch (ParseException ex) {
        java.util.logging.Logger.getLogger(KlasaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      TableColumn nowaKolumna = (zwrocKolumneZButtonem(zajeciaWmiesiacu.toString(), dataWkomorce, tab, table));
      table.getColumns().add(nowaKolumna);

    }

    return table;
  }

  private TableColumn zwrocKolumneZButtonem(String nazwaKol, Date dataWkomorce, Tab tab, TableView<Uczen> table) {
    TableColumn<Uczen, Uczen> colBtn = new TableColumn(nazwaKol);

    Callback<TableColumn<Uczen, Uczen>, TableCell<Uczen, Uczen>> cellFactory = new Callback<TableColumn<Uczen, Uczen>, TableCell<Uczen, Uczen>>() {
      @Override
      public TableCell<Uczen, Uczen> call(final TableColumn<Uczen, Uczen> param) {
        final TableCell<Uczen, Uczen> cell = new TableCell<Uczen, Uczen>() {

          private final Button btn = new Button("o");

          @Override
          public void updateItem(Uczen item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
              setGraphic(null);
            } else {
              //if (!item.getWartosc().equals("nieobecny")) {
              Set obecnosciUczniaKtoregoUcze = item.getObecnoscs();
              for (Iterator iterator = obecnosciUczniaKtoregoUcze.iterator(); iterator.hasNext();) {
                Obecnosc obecnosc = (Obecnosc) iterator.next();

                if (obecnosc.getData().equals(dataWkomorce) && obecnosc.getPrzedmiot().getNazwaPrzedmiotu().equals(tab.getText())) {
                  System.out.println("przedmioty porownanie: " + obecnosc.getPrzedmiot().getNazwaPrzedmiotu() + " z " + tab.getText());
                  btn.setText(obecnosc.getWartosc());
                  setGraphic(btn);
                } else {
                  //btn.setText("o");
                  setGraphic(btn);
                }

                btn.setOnAction((ActionEvent event) -> {

                  if (btn.getText().equals("o")) {
                    btn.setText("n");
                    Obecnosc nieobecny = obecnosc;
                    nieobecny.setPrzedmiot(new Przedmiot(tab.getText()));
                    nieobecny.setData(dataWkomorce);
                    nieobecny.setWartosc("n");
                    HibernateUtil.dodajNieobecnosc(nieobecny);
                    //table.refresh();

                  } else if (btn.getText().equals("n")) {

                    obecnosc.setWartosc("o");
//                  Obecnosc obecny = zwrocNieobecnoscZdanegoDnia(item, dataWkomorce);
//                  System.out.println(obecny.getUczen().getImie() + " wartosc: " + obecny.getWartosc());
//                  obecny.setWartosc("o");
                    HibernateUtil.usunNieobecnosc(obecnosc);
                    btn.setText("o");
                  }
                });

              }
            }

          }
        };
        return cell;
      }
    };
    colBtn.setCellFactory(cellFactory);

    colBtn.setCellValueFactory(data
            -> new ReadOnlyObjectWrapper<Uczen>(data.getValue()));
    return colBtn;
  }

// OCENY---------------------------
  private void stworzZakladkiOceny() {

    List<Przedmiot> przedmioty = zwrocPrzedmiotyKtorychUczeDanaKlase(klasa, pesel);
    tabsPane.getTabs().clear();
    for (Przedmiot przedmiot : przedmioty) {
      tabsPane.getTabs().add(stworzPojedynczaZakladke(przedmiot));

    }

  }

  private Tab stworzPojedynczaZakladke(Przedmiot przedmiot) {
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
//    TextField dataPole = new TextField();
//    dataPole.setMaxWidth(80);

         DatePicker datePicker = new DatePicker();
    datePicker.setConverter(new StringConverter<LocalDate>() {
      private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      @Override
      public String toString(LocalDate localDate) {
        if (localDate == null) {
          return "";
        }
        return dateTimeFormatter.format(localDate);
      }

      @Override
      public LocalDate fromString(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
          return null;
        }
        return LocalDate.parse(dateString, dateTimeFormatter);
      }
    });

     
    
    
    
    
    HBox textfieldy = new HBox();
    textfieldy.setSpacing(15);
    textfieldy.setPadding(new Insets(15, 20, 5, 10));
    textfieldy.setAlignment(Pos.CENTER);
    textfieldy.getChildren().addAll(ocenaPole, rodzajPole, datePicker);
    
    
   
   

    
    

    Button dodajOcene = new Button("Dodaj");
    dodajOcene.setVisible(true);
    dodajOcene.addEventHandler(MouseEvent.MOUSE_CLICKED, dodajOceneButtonHandler(table, ocenaPole, rodzajPole, datePicker, uczen, przedmiot, tab));

    Button edytujOcene = new Button("Edytuj");
    edytujOcene.setVisible(false);

    Button cancel = new Button("Cofnij");
    cancel.setVisible(false);

    HBox buttony = new HBox();
    buttony.setSpacing(15);
    buttony.setPadding(new Insets(15, 20, 5, 10));
    buttony.setAlignment(Pos.CENTER);
    buttony.getChildren().addAll(dodajOcene, edytujOcene, cancel);



    

    ustawiaczPane.getChildren().addAll(table, textfieldy, buttony);

    gagatekPane.getChildren().addAll(ustawiaczPane);

    table.addEventHandler(MouseEvent.MOUSE_CLICKED, dodajButtonyWypelnijTextFieldyHandler(table, ocenaPole, rodzajPole, dodajOcene, edytujOcene, cancel, uczen, przedmiot, tab, datePicker));

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

  // HANDLERY OCENY---------------------------
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

  private EventHandler dodajButtonyWypelnijTextFieldyHandler(TableView<Ocena> table, TextField ocena, TextField rodzaj, Button dodaj, Button edytuj, Button cancel, Uczen uczen, Przedmiot przedmiot, Tab tab, DatePicker datePicker) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        Ocena selectedItem = table.getSelectionModel().getSelectedItem();
        //sprawdz null pointera
        if (!selectedItem.toString().isEmpty()) {
          ocena.setText(selectedItem.getStopien().toString());
          rodzaj.setText(selectedItem.getRodzajOceny().getRodzajOceny());
          //data.setText(utilities.Utils.dateToString(selectedItem.getData()));
          
          LocalDate date = ((java.sql.Date) selectedItem.getData()).toLocalDate();
          
          datePicker.setValue(date);
          //data.setText(selectedItem.getData().toString());
          cancel.setVisible(true);
          dodaj.setVisible(false);
          edytuj.setVisible(true);
          edytuj.addEventHandler(MouseEvent.MOUSE_CLICKED, edytujOceneButtonHandler(table, ocena, rodzaj, datePicker, uczen, przedmiot, tab, selectedItem, dodaj, cancel, edytuj));
          //TableView<Ocena> table, TextField ocena, TextField rodzaj, TextField data, Uczen uczen, Przedmiot przedmiot, Tab tab, Ocena obiektOcenaDoEdycji, Button dodaj, Button cancel
          cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
              ocena.setText("");
              rodzaj.setText("");
              //data.setText("");
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

  private EventHandler dodajOceneButtonHandler(TableView<Ocena> table, TextField ocena, TextField rodzaj, DatePicker datePicker, Uczen uczen, Przedmiot przedmiot, Tab tab) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        Ocena obiektOcenaDoWstawienia = new Ocena();//przedmiot, rodzajOceny, Uczen uczen, Integer stopien, Date data);
        obiektOcenaDoWstawienia.setPrzedmiot(przedmiot);
        obiektOcenaDoWstawienia.setRodzajOceny(new RodzajOceny(rodzaj.getText()));
        obiektOcenaDoWstawienia.setStopien(Integer.valueOf(ocena.getText()));
        obiektOcenaDoWstawienia.setUczen(uczen);
        try {
          String dataString = datePicker.getValue().toString();
          obiektOcenaDoWstawienia.setData(utilities.Utils.returnDate(dataString));

        } catch (ParseException ex) {
          java.util.logging.Logger.getLogger(KlasaController.class
                  .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        wstawOcene(obiektOcenaDoWstawienia);
        odswiezTabele(table, przedmiot, tab, uczen);

      }
    };
    return eventHandler;
  }

  private EventHandler edytujOceneButtonHandler(TableView<Ocena> table, TextField ocena, TextField rodzaj, DatePicker datePicker, Uczen uczen, Przedmiot przedmiot, Tab tab, Ocena obiektOcenaDoEdycji, Button dodaj, Button cancel, Button edytuj) {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        obiektOcenaDoEdycji.setRodzajOceny(new RodzajOceny(rodzaj.getText()));
        obiektOcenaDoEdycji.setStopien(Integer.valueOf(ocena.getText()));
               try {
          String dataString = datePicker.getValue().toString();
          obiektOcenaDoEdycji.setData(utilities.Utils.returnDate(dataString));

        } catch (ParseException ex) {
          java.util.logging.Logger.getLogger(KlasaController.class
                  .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

  private EventHandler stworzTabeleOceny() {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        stworzZakladkiOceny();
        gagatekPane.setVisible(true);
        gagatek.setVisible(true);
      }
    };
    return eventHandler;
  }

  private EventHandler stworzTabeleObecnosci() {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

        stworzZakladkiZobecnosciami();
        gagatekPane.setVisible(false);
        gagatek.setVisible(false);
      }
    };

    return eventHandler;
  }

  // UTILS---------------------------
  public void odswiezTabele(TableView<Ocena> tableOcena, Przedmiot przedmiot, Tab tab, Uczen uczen) {

    int ktoryTabJestWybrany = tabsPane.getSelectionModel().getSelectedIndex();
    //SingleSelectionModel wybierz = new SingleSelectionModel();
    Tab odswiezonyTab = stworzPojedynczaZakladke(przedmiot);
    tabsPane.getTabs().remove(ktoryTabJestWybrany);
    tabsPane.getTabs().add(odswiezonyTab);
    tableOcena.refresh();
    // wybierz nowo dodanego taba
    SingleSelectionModel<Tab> selectionModel = tabsPane.getSelectionModel();
    selectionModel.select(odswiezonyTab); //select by object

    ObservableList<Ocena> data
            = FXCollections.observableArrayList(zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu(uczen, przedmiot));
    tableOcena.setItems(data);

  }

  private void wstawKlaseDoLabela(String klasa) {
    jakaKlasa.setText(klasa);
  }
}
