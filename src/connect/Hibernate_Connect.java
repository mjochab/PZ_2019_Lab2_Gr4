/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import mapping.Przedmiot;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Hibernate_Connect {
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
        Przedmiot przedmiot1 = new Przedmiot("Matma");
        Przedmiot przedmiot2 = new Przedmiot("Religia");
        Przedmiot przedmiot3 = new Przedmiot("Wychowanie fizyczne");
        
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
          applySettings(configuration.getProperties()).build(); 
        SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);      
        Session session = factory.openSession();       
        Transaction transaction = session.beginTransaction();

        
        transaction.commit();
session.save(przedmiot1);
        session.close();        
        factory.close();

        StandardServiceRegistryBuilder.destroy(serviceRegistry);
  }

}
