/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import mapping.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Hibernate_Connect  {

   public static void main(String[] args) throws ParseException {

        //long pesel = 32222222221L;
        //Obecnosc obecny = new Obecnosc(1,pesel,Utils.returnDate("15-12-2019"),true,"algebra_liniowa");


        //generator faktorii z klasy hibernateutil
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();       
        Transaction transaction = session.beginTransaction();

        

        //session.save(obecny);


        transaction.commit();
        session.close();        
        factory.close();














//        Configuration config = new Configuration();
//        config.configure();
//        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
//        factory = config.buildSessionFactory(serviceRegistry);
//        
//        
//        Session session = factory.openSession();
//        Transaction tx = null;
//        
//        try
//        {
//            tx = session.beginTransaction();
//            Przedmiot przedmiot = new Przedmiot();
//            przedmiot.setNazwaPrzedmiotu("TestujemyCzyDziala");        
//            
//            System.out.println("niby dodano");
//            tx.commit();
//            session.save(przedmiot);
//        } 
//        catch (HibernateException ex)
//        {
//            if(tx != null)
//                System.out.println("gowno");
//        }
//        finally
//        {
//            session.close();
//        }
//        
//        
        
  }

}
