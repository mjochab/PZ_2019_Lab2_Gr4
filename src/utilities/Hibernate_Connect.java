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
import java.util.List;
import mapping.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Hibernate_Connect {

    public static void main(String[] args) throws ParseException {
//
//        long pesel = 32222222221L;
//// do zrobienia konstruktory odpowiednie by ulatwily nam prace.
//        //Obecnosc obecny = new Obecnosc("algebra_liniowa",pesel,Utils.returnDate("15-12-2019"),true);
//
//        //generator faktorii z klasy hibernateutil
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        Session session = factory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        // session.save(obecny);
//        transaction.commit();
//        session.close();
//        factory.close();
        List<String> zwrocKlasy2 = utilities.HibernateUtil.zwrocKlasy();
        
        for (int i=0; i<zwrocKlasy2.size(); i++) {
            System.out.println(zwrocKlasy2.get(i).toString());
        }
        

    }

}
