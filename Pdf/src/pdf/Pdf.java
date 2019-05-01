/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Krystian
 */
public class Pdf {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       

    public static ArrayList<UczenPDF> zwrocOceny(Set oceny) {
        ArrayList<UczenPDF> listaPDF = new ArrayList<>();
        String[] nazwyPrzedmiotow = HibernateUtil.pobieranieNazwPrzedmiotow();
        UczenPDF uczenpdf;
        String ocenyUcznia;
        double sredniaOcen;

        for (String przedmiot : nazwyPrzedmiotow) {
            ocenyUcznia = UczenPDF.zwrocOceny(przedmiot, oceny);
            sredniaOcen = UczenPDF.obliczSrednia(przedmiot, oceny);
            uczenpdf = new UczenPDF(przedmiot, ocenyUcznia, sredniaOcen);
            listaPDF.add(uczenpdf);
        }

        return listaPDF;
    }

    public static void tworzeniePDF(Uczen uczen) {
        ArrayList<UczenPDF> listaPDF = zwrocOceny(uczen.getOcenas());
        listaPDF.forEach((u) -> {
            System.out.println(u.getNazwaPrzedmiotu() + " | " + u.getOceny() + " | " + u.getSrednia());
        });

    }
}
