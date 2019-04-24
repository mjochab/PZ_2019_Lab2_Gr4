package utilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import utilities.HibernateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mapping.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import static utilities.HibernateUtil.uzyskajKtoZalogowany;
import static utilities.HibernateUtil.uzyskajPeselZalogowany;
import static utilities.HibernateUtil.zwrocPrzedmiotyKtorychUczeDanaKlase;
import static utilities.HibernateUtil.zwrocUczniowZklasy;

public class HibernateTest {

    public static void main(String[] args) throws ParseException {

    /*List<Przedmiot> przedmioty=zwrocPrzedmiotyKtorychUczeDanaKlase("1a", 22222222221L);
    
    for(Przedmiot przedmiot:przedmioty){
        System.out.println(przedmiot.getNazwaPrzedmiotu());
    }*/
     System.out.println(uzyskajKtoZalogowany(22222222225L));
     System.out.println(uzyskajPeselZalogowany("login","pass"));

    }
}
