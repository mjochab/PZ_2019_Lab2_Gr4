package utilities;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mapping.Uczen;
import mapping.Zajecia;
import oknoNauczyciel.UczenPDF;
import pdf.Pdf;
import java.io.FileOutputStream;
import java.util.Date;


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
  /**
   *
   * @param data
   * @return data sparsowana z stringa
   * @throws ParseException
   */
  public static Date returnDate(String data) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date date = format.parse(data);
    return date;
  }

  /**
   *
   * @param view - widok tabeli której rozmiar należy zmienić.
   */
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

  // ------------------------- WSTAWIANIE PLANU ZAJEĆ DO TABELKI ------------------------- //
  /**
   * Metoda wstawiająca godziny zajęć do odpowiedniej kolumny
   *
   * @param godzina - ArrayList typu String, lista godzin
   * @param kol - Table Column typu (Integer, String), kolumna do której
   * wartosci z listy <b>godzna</b> zostaną wstawione.
   */
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

  /**
   * Metoda zwracająca godziny (bez powtórzeń) zajeć.
   *
   * @param zajecia - ArrayList typu Zajecia, lista zwierająca zajęcia danego
   * ucznia
   * @return ArrayList typu String
   */
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

  /**
   * Metoda sortująca zajęcia po godzinie od najmniejszej do największej.
   *
   * @param zajecia - typ Set, zbiór zajęć danego ucznia.
   * @return ArrayList typu Zajecia.
   */
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

  /**
   * Metoda wstawiająca zajęcia z danego dna do odpowiedniej kolumny.
   *
   * @param kol - TableColumn (Integer, String) kolumna do której zajęcia z
   * danego dnia są wstawiane.
   * @param dzien - ArrayList typu String zawierająca dni tygodnia.
   */
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

  /**
   * Metoda pobierająca zajęcia z danego dnia tygodnia, jeśli występuje przerwa
   * między zajęciami wstawiany jest null.
   *
   * @param dzien - dany dzień tygodnia typu String,
   * @param zajeciaPosortowane - ArrayList typu Zajecia, posortowana lista zajęć
   * od najmniejszej godziny do największej,
   * @param godzina - ArrayList typu String zawierająca godziny zajęć ( od
   * najmniejszej do największej).
   * @return - ArrayList typu String zawierająca zajęcia danego dnia.
   */
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

  /**
   *
   * @param data
   * @return data w stringu zmieniona z Date daty
   * @throws ParseException
   */
  public static String dateToString(Date data) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(data);
    return date;
  }

  /**
   * Metoda dostaje liste dni tygodnia w ktorych dany nauczyciel ma zajecia,
   * nastepnie tworzy daty z wybranych dni tygodnia w danym miesiacu danego roku
   *
   * @param year
   * @param month
   * @param ktoreDniZwrocic lista integerow, 1-poniedzialek, 2-wtorek itd
   * @return daty w ktorych dany nauczyciel ma zajecia
   */
  public static List<Integer> zwrocDatyWktorychMamZajecia(int year, int month, List<Integer> ktoreDniZwrocic) {

    List<LocalDate> datyZajec = new ArrayList<>();
    List<Integer> kiedyMamZajecia = new ArrayList<>();
    LocalDate today = LocalDate.of(year, month, 1);
    LocalDate miesiacPozniej = today.plusMonths(1).minusDays(1);

    LocalDate localDate = today;

    while (localDate.isBefore(miesiacPozniej)) {
      int dow = localDate.getDayOfWeek().getValue();

      if (ktoreDniZwrocic.contains(dow)) {
        datyZajec.add(localDate);
        kiedyMamZajecia.add(localDate.getDayOfMonth());

      }

      // Set-up the next loop.
      localDate = localDate.plusDays(1);
    }

    return kiedyMamZajecia;

  }

  /**
   * Metoda służąca do dopasowania ocen ucznia do danych przedmiotów oraz
   * obliczenia średniej z nich.
   *
   * @param oceny - oceny danego ucznia
   * @return ArrayList typu UczenPDF zawiwerająca obiekty przedmiotów wraz z
   * ocenami i średnią.
   */
  public static ArrayList<UczenPDF> zwrocOceny(Set oceny) {
    ArrayList<UczenPDF> listaPDF = new ArrayList<>();
    String[] nazwyPrzedmiotow = HibernateUtil.pobieranieNazwPrzedmiotow();
    UczenPDF uczenpdf;
    String ocenyUcznia;
    String sredniaOcen;

    for (String przedmiot : nazwyPrzedmiotow) {
      ocenyUcznia = UczenPDF.zwrocOceny(przedmiot, oceny);
      sredniaOcen = UczenPDF.obliczSrednia(przedmiot, oceny);
      uczenpdf = new UczenPDF(przedmiot, ocenyUcznia, sredniaOcen);
      listaPDF.add(uczenpdf);
    }

    return listaPDF;
  }

  /**
   * Metoda tworząca pfd, wywołująca metoda z biblioteki Pdf.
   *
   * @param uczen - Typ Uczen, uczen dla którego pdf zostanie utworzony,
   * @throws FileNotFoundException
   */
  public static void tworzeniePDF(Uczen uczen) throws FileNotFoundException {
    ArrayList<UczenPDF> listaPDF = zwrocOceny(uczen.getOcenas());
    String imie = uczen.getImie();
    String nazwisko = uczen.getNazwisko();

    ArrayList<String> przedmioty = new ArrayList<String>();
    ArrayList<String> oceny = new ArrayList<String>();
    ArrayList<String> srednia = new ArrayList<String>();

    for (UczenPDF l : listaPDF) {
      przedmioty.add(l.getNazwaPrzedmiotu());
      oceny.add(l.getOceny());
      srednia.add(String.valueOf(l.getSrednia()));
    }
    Pdf.tworzeniePDF(imie, nazwisko, przedmioty, oceny, srednia);

  }
}
