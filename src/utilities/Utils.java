package utilities;

import Okna.LogowanieController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mapping.Uczen;
import mapping.Zajecia;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Veth
 */
public class Utils {

  //data gotowa do wstawienia przez nauczyciela
  public static Date returnDate(String data) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date date = format.parse(data);
    return date;
  }

  public static void customResize(TableView<?> view) {

    AtomicLong width = new AtomicLong();
    view.getColumns().forEach(col -> {
      width.addAndGet((long) col.getWidth());
    });
    double tableWidth = view.getWidth();

    if (tableWidth > width.get()) {
      view.getColumns().forEach(col -> {
        col.setPrefWidth(col.getWidth() + ((tableWidth - width.get()) / view.getColumns().size()));
      });
    }


  }

  public static void zwrocWartoscStringZKomorki(TableView<Uczen> table, int column, int row) {
    String a = table.getColumns().get(column).getCellObservableValue(row).getValue().toString();
  }




    // ------------------------- WSTAWIANIE PLANU ZAJEĆ DO TABELKI ------------------------- //
    public static void wstawianieGodziny(ArrayList<String> godzina, TableColumn<Integer, String> kol) {
        kol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            if (rowIndex >= godzina.size()) {
                return null;
            } else {
                return new ReadOnlyStringWrapper(godzina.get(rowIndex));
            }
        });
    }

    public static ArrayList<String> pobierzGodziny(ArrayList<Zajecia> zajecia) {
        ArrayList<String> lista = new ArrayList<String>();
        Iterator<Zajecia> it = zajecia.iterator();

        while (it.hasNext()) {
            Zajecia ob = it.next();
            if (lista.contains(ob.getGodzina().toString())) {
            } else {
                lista.add(ob.getGodzina().toString());
            }
        }
        return lista;
    }

    public static ArrayList<Zajecia> posortujZajecia(Set zajecia) {
        ArrayList<Zajecia> zajeciaPosortowane = new ArrayList<Zajecia>();

        Iterator<Zajecia> it = zajecia.iterator();
        while (it.hasNext()) {
            Zajecia ob = it.next();
            zajeciaPosortowane.add(ob);
        }
        Comparator<Zajecia> porownajPoGodzinie = (Zajecia o1, Zajecia o2)
                -> o1.getGodzina().compareTo(o2.getGodzina());

        Collections.sort(zajeciaPosortowane, porownajPoGodzinie);
        return zajeciaPosortowane;
    }

    public static void wstawianieZajecDoKolumn(TableColumn<Integer, String> kol, ArrayList<String> dzien) {
        kol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            if (rowIndex >= dzien.size()) {
                return null;
            } else {
                return new ReadOnlyStringWrapper(dzien.get(rowIndex));
            }
        });
    }

    public static ArrayList<String> pobierzZajeciaDnia(String dzien, ArrayList<Zajecia> zajeciaPosortowane,
            ArrayList<String> godzina) {

        ArrayList<String> zajeciaDnia = new ArrayList<String>();
        ArrayList<Zajecia> zajeciaDniaObiekt = new ArrayList<Zajecia>();
        Iterator<Zajecia> it = zajeciaPosortowane.iterator();
        Iterator<String> itG = godzina.iterator();
        // Pobierane są zajęcia z danego dnia.
        while (it.hasNext()) {
            Zajecia ob = it.next();

            if (ob.getDzien().equals(dzien)) {
                zajeciaDniaObiekt.add(ob);
            }
        }
        ListIterator<Zajecia> itO = zajeciaDniaObiekt.listIterator();
        // Sprawdzanie czy dla danej godziny jest przedmiot, jeśli nie wstaw null, jeśli tak dodaj do listy
        while (itG.hasNext() && itO.hasNext()) {
            String x = itG.next();
            Zajecia ob = itO.next();
            if (ob.getGodzina().toString().equals(x)) {
                zajeciaDnia.add(ob.getPrzedmiot().getNazwaPrzedmiotu());
            } else {
                zajeciaDnia.add(null);
                itO.previous();
            }
        }
        return zajeciaDnia;
    }

}
