package utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mapping.Klasa;
import mapping.Ocena;
import mapping.Przedmiot;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.persister.entity.AbstractEntityPersister;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static String[] pobieranieNazwPrzedmiotow() {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        Transaction tx = null;
        String[] nazwa = null;
        int i = 0;

        try {
            tx = session.beginTransaction();
            List nazwaPrzedmiotu = session.createQuery("FROM Przedmiot").list();
            nazwa = new String[nazwaPrzedmiotu.size()];
            for (Iterator iterator = nazwaPrzedmiotu.iterator(); iterator.hasNext();) {
                Przedmiot przedmiot = (Przedmiot) iterator.next();
                nazwa[i] = przedmiot.getNazwaPrzedmiotu();
                i++;
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return nazwa;
        }
    }
    
    public String[] zwrocKlasy( ) {
        
       Session session = getSessionFactory().openSession();
       Transaction tx = null;
       String [] wynik = null;
       try {
          tx = session.beginTransaction();
          Criteria cr = session.createCriteria(Klasa.class);
          // ponizej mozna dodac restrykcje
          
          List klasy = cr.list();
          int i=0;
          for (Iterator iterator = klasy.iterator(); iterator.hasNext();){ 
             Klasa klasa = (Klasa) iterator.next(); 
             wynik[i]=klasa.getNazwaKlasy();
             i++;
             
          }
          tx.commit();
       } catch (HibernateException e) {
          if (tx!=null) tx.rollback();
          e.printStackTrace(); 
       } finally {
          session.close(); 
       }
       return wynik;
   }
    
   public static List<String> zwrocKlasy2(){
    EntityManager entityManager = getSessionFactory().createEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

CriteriaQuery<String> criteria = builder.createQuery( String.class );
Root<Klasa> root = criteria.from( Klasa.class );
criteria.select( root.get("nazwaKlasy"));
//criteria.where( builder.equal( root.get( Person_.name ), "John Doe" ) );

List<String> klasy = entityManager.createQuery( criteria ).getResultList();
       
return klasy;      
}

}
