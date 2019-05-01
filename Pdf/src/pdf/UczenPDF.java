/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

import static java.lang.Math.abs;
import java.util.Iterator;
import java.util.Set;
import mapping.Ocena;

/**
 *
 * @author Krystian
 */
public class UczenPDF {

    private String nazwaPrzedmiotu;
    private String oceny;
    private double srednia;

    public UczenPDF(String nazwaPrzedmiotu, String oceny, double srednia) {
        this.nazwaPrzedmiotu = nazwaPrzedmiotu;
        this.oceny = oceny;
        this.srednia = srednia;
    }

    public void setNazwaPrzedmiotu(String nazwaPrzedmiotu) {
        this.nazwaPrzedmiotu = nazwaPrzedmiotu;
    }

    public void setOceny(String oceny) {
        this.oceny = oceny;
    }

    public void setSrednia(double srednia) {
        this.srednia = srednia;
    }

    public String getNazwaPrzedmiotu() {
        return nazwaPrzedmiotu;
    }

    public String getOceny() {
        return oceny;
    }

    public double getSrednia() {
        return srednia;
    }

    public static String zwrocOceny(String przedmiot, Set oceny) {
        String ocenyUcznia = "";
        Iterator<Ocena> it = oceny.iterator();
        double pojedynczaOcena;
        while (it.hasNext()) {
            Ocena ocena = it.next();
            if (ocena.getPrzedmiot().getNazwaPrzedmiotu().equals(przedmiot)) {
                pojedynczaOcena = ocena.getStopien();
                if (pojedynczaOcena < 0 ){
                    pojedynczaOcena = abs(pojedynczaOcena)-0.25;
                }
                ocenyUcznia += String.valueOf(pojedynczaOcena) + ",";
            }
        }
        return ocenyUcznia;
    }

    public static Double obliczSrednia(String przedmiot, Set oceny) {
        String ocenyUczniaString = zwrocOceny(przedmiot, oceny);
        double sumaOcen = 0;
        double sredniaOcen = 0;

        if (ocenyUczniaString.equals("")) {
            return sredniaOcen;
        }

        String[] ocenyUcznia = zwrocOceny(przedmiot, oceny).split(",");
        for (String ocena : ocenyUcznia) {
            sumaOcen += Double.valueOf(ocena);
        }

        sredniaOcen = sumaOcen / ocenyUcznia.length;
        return sredniaOcen;
    }

}
