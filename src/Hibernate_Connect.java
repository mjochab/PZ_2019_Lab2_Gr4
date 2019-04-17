/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import mapping.*;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Hibernate_Connect  {
    
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder() .configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } 
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
/*
    private static final SessionFactory sessionFactory;   
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    } 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    */
   public static void main(String[] args) {
        Przedmiot przedmiot1 = new Przedmiot("Dzialam?");
        Przedmiot przedmiot2 = new Przedmiot("Religia");
        Przedmiot przedmiot3 = new Przedmiot("Wychowanie fizyczne");
        
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();       
        Transaction transaction = session.beginTransaction();

        

//session.save(przedmiot1);
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
