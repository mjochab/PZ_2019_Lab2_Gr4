/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mapping.Ocena;
import mapping.Przedmiot;
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
      tabA.setContent(stworzTabeleZkolumnamiOceny(przedmiot.getNazwaPrzedmiotu()));
      tabsPane.getTabs().removeAll();
      tabsPane.getTabs().add(tabA);

    }

  }

  private TableView stworzTabeleZocenami(String przedmiot) {

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

    for (Uczen uczen : uczniowie) {

      Set oceny = uczen.getOcenas();

      //ObservableList<Integer> ocenyUcznia = FXCollections.observableArrayList();
      for (Iterator iterator = oceny.iterator(); iterator.hasNext();) {
        Ocena ocena = (Ocena) iterator.next();
        System.out.println(ocena.getUczen().getNazwisko() + " " + ocena.getPrzedmiot().getNazwaPrzedmiotu() + " " + ocena.getStopien());
      }

    }
    TableColumn ocenyCol = new TableColumn("Oceny");
    ocenyCol.setMinWidth(200);

    ocenyCol.setCellValueFactory(new Callback<CellDataFeatures<Uczen, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Uczen, String> data) {
        //tutaj robic kolejna kolumne w petli zaleznie ile jest ocen
        StringProperty sp = new SimpleStringProperty();
        // jakby przechowac id oceny i robic kolejne kolumny to potem z hibernate moge sie odniesc i zmienic poszczegolne oceny
        sp.setValue(String.valueOf(
                //magic
                wyliczOcenyZmojegoPrzedmiotuZapiszJeDoStringa(data, przedmiot)
        ));
        return sp;
      }
    });
    ObservableList<Uczen> data
            = FXCollections.observableArrayList(uczniowie);
    table.setItems(data);
    table.getColumns().addAll(firstNameCol, lastNameCol, ocenyCol);
    customResize(table);
    return table;

  }

  // do roboty
  private TableColumn stworzKolumneUcznia(String tytulIgetter) {
    TableColumn kolumna = new TableColumn(tytulIgetter);
    kolumna.setMinWidth(100);
    kolumna.setCellValueFactory(
            new PropertyValueFactory<Uczen, String>(tytulIgetter));

    return kolumna;
  }

  private TableView stworzTabeleZkolumnamiOceny(String przedmiot) {

    //5 kolumn - sprawdzian, kartkowka, odpowiedz, referat, zadanie domowe
    TableView<Uczen> table = new TableView<Uczen>();

    ObservableList<Uczen> data
            = FXCollections.observableArrayList(uczniowie);
    table.setItems(data);
    table.getColumns().addAll(stworzKolumneUcznia("Imie"), stworzKolumneUcznia("Nazwisko"));

    List<String> rodzajeOcen = zwrocRodzajeOcen();
    List<TableColumn> kolumnyzOcenami = new ArrayList<>();
    int i = 0;
    // tworz tyle kolumn ile rodzajow ocen
    for (String rodzajOceny : rodzajeOcen) {
      System.out.println("rodzaj oceny: " + rodzajOceny);
      kolumnyzOcenami.add(new TableColumn(rodzajOceny));
      // wpisywanie ocen do odpowiednich kolumn
      kolumnyzOcenami.get(i).setCellValueFactory(new Callback<CellDataFeatures<Uczen, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Uczen, String> data) {
          // dorobic liste ktora przechowuje po kolei obiekty z ocenami
          StringProperty ocenyUczniaDoWyswietlenia = new SimpleStringProperty();
          SimpleObjectProperty obiektZOcenaUcznia = new SimpleObjectProperty();
          // oceny uczniow danej klasy z danego przedmiotu;
          Set ocenyUczniowKtorychUcze = data.getValue().getOcenas();
          String ocenydoWyswietlenia = "";
          for (Iterator iterator = ocenyUczniowKtorychUcze.iterator(); iterator.hasNext();) {
            Ocena ocena = (Ocena) iterator.next();

            if (ocena.getRodzajOceny().getRodzajOceny().equals(rodzajOceny) && ocena.getPrzedmiot().getNazwaPrzedmiotu().equals(przedmiot)) {
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

    customResize(table);
    table.setEditable(true);
    return table;

  }

  public static String wyliczOcenyZmojegoPrzedmiotuZapiszJeDoStringa(CellDataFeatures<Uczen, String> daneZkomorkiTabeli, String przedmiot) {
    // pesel do dania dynamicznie
    // przedmiot do wziecia z taba
    // List<Przedmiot> przedmiot = zwrocPrzedmiotyKtorychUczeDanaKlase(klasa,22222222220L);
    String oceny = "";
    Set wszystkieOcenyUcznia = daneZkomorkiTabeli.getValue().getOcenas();

    for (Iterator iterator = wszystkieOcenyUcznia.iterator(); iterator.hasNext();) {
      Ocena ocena = (Ocena) iterator.next();

      if (ocena.getPrzedmiot().getNazwaPrzedmiotu().equals(przedmiot)) {

        oceny = oceny + ocena.getStopien() + ", ";
      }

    }

    return oceny;
  }

  private String wyliczOcenyZmojegoPrzedmiotu(String przedmiot) {

    String oceny = "";

    for (Uczen uczen : uczniowie) {
      Set ocenyUcznia = uczen.getOcenas();
      for (Iterator it = ocenyUcznia.iterator(); it.hasNext();) {
        Ocena ocena = (Ocena) it.next();
        if (ocena.getPrzedmiot().getNazwaPrzedmiotu().equals(przedmiot)) {
        }
      }
    }

    return oceny;
  }

}
