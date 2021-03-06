package utilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import utilities.HibernateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mapping.*;
import utilities.HibernateUtil.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import static utilities.HibernateUtil.zwrocKlasyKtorychUcze;
import static utilities.HibernateUtil.zwrocKtoZalogowany;

import static utilities.HibernateUtil.uzyskajKtoZalogowany;
import static utilities.HibernateUtil.uzyskajLoginZalogowany;
import static utilities.HibernateUtil.uzyskajPeselZalogowany;
import static utilities.HibernateUtil.zwrocPrzedmiotyKtorychUczeDanaKlase;
import static utilities.HibernateUtil.zwrocRodzajeOcen;
import static utilities.HibernateUtil.zwrocUczniowZklasy;
import static utilities.HibernateUtil.zwrocNauczyciela;
import static utilities.HibernateUtil.zwrocPrzedmiotyKtorychUczeDanaKlase;
import static utilities.HibernateUtil.zwrocRodzajeOcen;
import static utilities.HibernateUtil.zwrocUczniowZklasy;
import static utilities.HibernateUtil.zwrocWJakieDniTygodniaMamZajecia;
import static utilities.Utils.zwrocDatyWktorychMamZajecia;

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

  public static List<Klasa> zwrocKlasyKtorychUcze(Long pesel) {

    CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
    Root<Zajecia> root = criteria.from(Zajecia.class);
    criteria.select(root.get("klasa"));
    criteria.where(builder.equal(root.get("nauczyciel"), pesel));
    criteria.distinct(true);
    List<Klasa> klasy = entityManager.createQuery(criteria).getResultList();

    return klasy;
  }

  public static List<Klasa> zwrocObecnosci(Long pesel) {

    CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
    Root<Zajecia> root = criteria.from(Zajecia.class);
    criteria.select(root.get("klasa"));
    criteria.where(builder.equal(root.get("nauczyciel"), pesel));
    criteria.distinct(true);
    List<Klasa> klasy = entityManager.createQuery(criteria).getResultList();

    return klasy;
  }

  public static void testyQuery() {
    Query query = entityManager.createQuery(
            "SELECT ocenas.opis from Uczen u JOIN fetch u.ocenas o where u.imie='Gniewomir' and o.stopien=32222222221");
    //FROM com.abc.model.Review r LEFT JOIN fetch r.employees emp WHERE r.id = 1 AND ( emp.id = 11 )
//      select p from Person p 
// inner join p.cars car
// where car.model = :model
    String resultList = (String) query.getSingleResult();
    System.out.println(resultList);

  }

   public static List<Obecnosc> zwrocObecnosciZprzedmiotu (Przedmiot przedmiot, List<Uczen> uczniowie ) {

    
    
    
    CriteriaQuery<Obecnosc> criteria = builder.createQuery(Obecnosc.class);
    Root<Obecnosc> root = criteria.from(Obecnosc.class);
    criteria.select(root);
     criteria.where(root.get("uczen").in(uczniowie));
    criteria.where(builder.equal(root.get("przedmiot"), przedmiot));
    List<Obecnosc> obecnosci = entityManager.createQuery(criteria).getResultList();
     for (Obecnosc obecnosc : obecnosci) {
       System.out.println(obecnosc.getWartosc());
       
     }
    return obecnosci;

  }
 public static List<Uczen> zwrocUczniowZklasy(String klasa) {

    EntityManager em = sessionFactory.createEntityManager();
    CriteriaBuilder b = em.getCriteriaBuilder();
    CriteriaQuery<SkladKlasy> criteria = b.createQuery(SkladKlasy.class);
    Root<Klasa> root = criteria.from(Klasa.class);
    criteria.select(root.get("skladKlasies"));
    criteria.where(b.equal(root.get("nazwaKlasy"), klasa));

    List<SkladKlasy> skladKlasy = em.createQuery(criteria).getResultList();
    List<Uczen> uczniowie = new ArrayList<>();

    for (SkladKlasy uczen : skladKlasy) {
      uczniowie.add(uczen.getUczen());
    }
    return uczniowie;
  }


  public static void main(String[] args) throws ParseException {
    Przedmiot przedmiot = new Przedmiot("algebra_liniowa");
    List<Uczen> uczniowie = new ArrayList<>();
    uczniowie = zwrocUczniowZklasy("1a");
    zwrocObecnosciZprzedmiotu(przedmiot, uczniowie);
    
  }
}
