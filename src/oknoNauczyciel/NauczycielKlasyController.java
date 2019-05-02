/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.HibernateUtil;
import static utilities.HibernateUtil.zwrocKlaseKtoraWychowuje;

public class NauczycielKlasyController implements Initializable {

  //W oczekiwaniu na przekazanie wartosci z logowania, tymczasowe stałe:
  private Long pesel = 22222222220L;
  private String username = "Wojtus";

  @FXML
  private Button klasa1;
  @FXML
  private Button klasa2;
  @FXML
  private Button wylogujbtn;
  @FXML
  private Label userid;
  @FXML
  private Pane panelButtonow;
  @FXML
  private AnchorPane rootPane;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    wybierzKlaseButtony();
    wstawUseraDoZalogowanoJako(username);
    // TODO
  }

  @FXML
  private void handleButtonAction(ActionEvent event) throws IOException {

  }

  @FXML
  private void logout(ActionEvent event) throws IOException {
    AnchorPane pane = FXMLLoader.load(getClass().getResource("/Okna/Logowanie.fxml"));
    rootPane.getChildren().setAll(pane);
  }

  private void wybierzKlaseButtony() {
    VBox vb = new VBox();
    String[] nazwyKlas = utilities.HibernateUtil.zwrocNazwyKlasKtorychUcze(pesel);
    Button[] listaButtonow = new Button[nazwyKlas.length];
    for (int i = 0; i < nazwyKlas.length; i++) {
      Button b = new Button(nazwyKlas[i]);
      b.setId("klasax" + i);
      b.setText(nazwyKlas[i]);
      b.addEventHandler(MouseEvent.MOUSE_CLICKED,
              (event -> {
                try {
                  zaladujKlase(b);
                } catch (IOException ex) {
                  Logger.getLogger(NauczycielKlasyController.class.getName()).log(Level.SEVERE, null, ex);
                }
              }));
      listaButtonow[i] = b;
    }
    String klasaKtoraWychowuje = "";
    if ((klasaKtoraWychowuje = zwrocKlaseKtoraWychowuje(pesel)).equals("")) {

    } else {
      Button wychowankowie = new Button("Wychowankowie");
      wychowankowie.addEventHandler(MouseEvent.MOUSE_CLICKED, przejdzDoWychowankowHandler());
      vb.getChildren().add(wychowankowie);

    }

    HBox hb = new HBox();
    hb.setSpacing(15);
    hb.setPadding(new Insets(15, 20, 5, 10));
    hb.setAlignment(Pos.CENTER);
    hb.getChildren().addAll(listaButtonow);
    vb.getChildren().addAll(hb);
    vb.setSpacing(15);
    vb.setPadding(new Insets(15, 20, 5, 10));
    vb.setAlignment(Pos.CENTER);
    panelButtonow.getChildren().clear();
    panelButtonow.getChildren().addAll(vb);

  }

  private EventHandler przejdzDoWychowankowHandler() {

    EventHandler eventHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        try {
          zaladujOknoWychowawcy();
        } catch (IOException ex) {
          java.util.logging.Logger.getLogger(KlasaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      }
    };
    return eventHandler;

  }

  private void zaladujOknoWychowawcy() throws IOException {

    Stage st = new Stage();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Wychowawca.fxml"));
    Region root = (Region) fxmlLoader.load();

    Scene scene = new Scene(root);
    st.setScene(scene);

    WychowawcaController mainController = fxmlLoader.<WychowawcaController>getController();
    mainController.przekazKlaseIusername(zwrocKlaseKtoraWychowuje(pesel), username, pesel);

    st.show();

  }

  public void przekazNazweUzytkownikaIPesel(String username, Long pesel) {

    this.username = username;
    this.pesel = pesel;

  }

  public void wstawUseraDoZalogowanoJako(String username) {

    userid.setText(username);

  }

  private void zaladujKlase(Button b) throws IOException {

    Stage st = new Stage();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Klasa.fxml"));
    Region root = (Region) fxmlLoader.load();

    Scene scene = new Scene(root);
    st.setScene(scene);

    KlasaController mainController = fxmlLoader.<KlasaController>getController();
    mainController.przekazKlaseIusername(b.getText(), username, pesel);

    st.show();

  }

}
