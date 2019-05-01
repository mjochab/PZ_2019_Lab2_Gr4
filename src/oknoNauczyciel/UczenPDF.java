/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oknoNauczyciel;

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
    private String srednia;

    public UczenPDF(String nazwaPrzedmiotu, String oceny, String srednia) {
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

    public void setSrednia(String srednia) {
        this.srednia = srednia;
    }

    public String getNazwaPrzedmiotu() {
        return nazwaPrzedmiotu;
    }

    public String getOceny() {
        return oceny;
    }

    public String getSrednia() {
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

    public static String obliczSrednia(String przedmiot, Set oceny) {
        String ocenyUczniaString = zwrocOceny(przedmiot, oceny);
        double sumaOcen = 0;
        double sredniaOcen = 0;

        if (ocenyUczniaString.equals("")) {
            return "";
        }

        String[] ocenyUcznia = zwrocOceny(przedmiot, oceny).split(",");
        for (String ocena : ocenyUcznia) {
            sumaOcen += Double.valueOf(ocena);
        }

        sredniaOcen = sumaOcen / ocenyUcznia.length;
        if(sredniaOcen == 0){
            return "";
        }
        return String.valueOf(sredniaOcen);
    }

}
