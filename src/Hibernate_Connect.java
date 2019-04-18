/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import mapping.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Hibernate_Connect  {

   public static void main(String[] args) {
        Przedmiot przedmiot1 = new Przedmiot("Dzialam?");
        Przedmiot przedmiot2 = new Przedmiot("Religia");
        Przedmiot przedmiot3 = new Przedmiot("Wychowanie fizyczne");
        
        //generator faktorii z klasy hibernateutil
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();       
        Transaction transaction = session.beginTransaction();

        

        session.save(przedmiot1);
        session.save(przedmiot1);
        session.save(przedmiot1);

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
