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
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mapping.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import static utilities.HibernateUtil.zwrocKlasyKtorychUcze;
import static utilities.HibernateUtil.zwrocKtoZalogowany;
import static utilities.HibernateUtil.zwrocPrzedmiotyKtorychUczeDanaKlase;
import static utilities.HibernateUtil.zwrocUczniowZklasy;

public class HibernateTest {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final EntityManager entityManager = sessionFactory.createEntityManager();
    private static final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

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

//    public static List<Klasa> zwrocKlasyKtorychUcze(Long pesel) {
//
//        CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
//        Root<Nauczyciel> root = criteria.from(Nauczyciel.class);
//        criteria.select(root.get("klasas"));
//        criteria.where(builder.equal(root.get("pesel"), pesel));
//
//        List<Klasa> klasy = entityManager.createQuery(criteria).getResultList();
//        return klasy;
//    }

//    public static String[] zwrocNazwyKlasKtorychUcze(Long pesel) {
//
//        List<Klasa> klasy = zwrocKlasyKtorychUcze(pesel);
//
//        String nazwyKlas[] = new String[klasy.size()];
//        int i = 0;
//        for (Klasa klasa : klasy) {
//            nazwyKlas[i] = klasa.getNazwaKlasy();
//
//            i++;
//        }
//
//        return nazwyKlas;
  //  }
        public static List<Klasa> zwrocKlasyKtorychUcze(Long pesel) {

        CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
        Root<Zajecia> root = criteria.from(Zajecia.class);
        criteria.select(root.get("klasa"));
        criteria.where(builder.equal(root.get("nauczyciel"), pesel));
        criteria.distinct(true);
        List<Klasa> klasy = entityManager.createQuery(criteria).getResultList();
        
        return klasy;
    }
            
    public static void main(String[] args) throws ParseException {

        List<Klasa> klasy = zwrocKlasyKtorychUcze(22222222226L);
        System.out.println(klasy.isEmpty());
        String nazwyKlas[] = new String[klasy.size()];
        int i = 0;
        for (Klasa klasa : klasy) {
            
            System.out.println(klasa.getNazwaKlasy());

            i++;
        }

    }

}
