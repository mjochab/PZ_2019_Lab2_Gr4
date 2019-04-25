/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import mapping.Przedmiot;
import mapping.Uczen;
import static oknoNauczyciel.KlasaController.wyliczOcenyZmojegoPrzedmiotuZapiszJeDoStringa;
import static utilities.HibernateUtil.zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu;
import static utilities.Utils.customResize;

/**
 *
 * @author Veth
 */
public class LegacyFunctions {
    
 private TableView stworzTabeleZocenami(Przedmiot przedmiot) {

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
                wyliczOcenyZmojegoPrzedmiotuZapiszJeDoStringa(data, przedmiot.getNazwaPrzedmiotu())
        ));
        return sp;
      }
    });
    //ObservableList<Uczen> data
      //      = FXCollections.observableArrayList(uczniowie);
    //table.setItems(data);
    table.getColumns().addAll(firstNameCol, lastNameCol, ocenyCol);
    customResize(table);

    // zmiana ocen w toku   
 table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        Uczen selectedItem = table.getSelectionModel().getSelectedItem();
                
                System.out.println("That is selected item : "+selectedItem);
                //gagatek.setText(selectedItem.getNazwisko());
                zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu(selectedItem,przedmiot);
                if (selectedItem.equals(null)) {
                  
                  System.out.println(" No item selected");
                  
                  
                } else {
                  System.out.println("Index to be deleted:" + selectedItem.getNazwisko());
                  
                  //Here was my database data retrieving and selectd
                  // item deleted and then table refresh
                  table.refresh();
                  
                  return;
                }
      }
    });
//    table.getFocusModel().focusedCellProperty().addListener(
//            new ChangeListener<TablePosition>() {
//      @Override
//      public void changed(ObservableValue<? extends TablePosition> observable,
//              TablePosition oldPos, TablePosition pos) {
//        int row = pos.getRow();
//        int column = pos.getColumn();
//        String selectedValue = "";
//        System.out.println("wiersz " + row);
//        System.out.println("kolumna " + column);
//        zwrocWartoscStringZKomorki(table, column, row);
//
//      }
//    });

    return table;

  }   
    
    
    
    
    
}
