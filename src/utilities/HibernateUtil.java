package utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import mapping.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Projections;
import org.hibernate.persister.entity.AbstractEntityPersister;

public class HibernateUtil {

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

  public static String zwrocKtoZalogowany(Long pesel) {

    CriteriaQuery<String> criteria = builder.createQuery(String.class);
    Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
    criteria.select(root.get("kto"));
    criteria.where(builder.equal(root.get("pesel"), pesel));

    String kto = entityManager.createQuery(criteria).getSingleResult();
    return kto;

  }

  public static List<String> zwrocWszystkieKlasy() {
    // najpierw co zwracamy
    CriteriaQuery<String> criteria = builder.createQuery(String.class);
    // skad, z jakiego mapowania
    Root<Klasa> root = criteria.from(Klasa.class);
    // ktora kolumna
    criteria.select(root.get("nazwaKlasy"));
    // dodatkowe kryteria
    //criteria.where( builder.equal( root.get( Person_.name ), "John Doe" ) );
    // zawsze zwraca liste z typem ktory dalismy na samej gorze.
    List<String> klasy = entityManager.createQuery(criteria).getResultList();

    return klasy;
  }

  public static List<Klasa> zwrocKlaseKtoraWychowuje(Long pesel) {
    CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
    Root<Nauczyciel> root = criteria.from(Nauczyciel.class);
    criteria.select(root.get("klasas"));
    criteria.where(builder.equal(root.get("pesel"), pesel));

    List<Klasa> klasy = entityManager.createQuery(criteria).getResultList();
    return klasy;
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

  public static String[] zwrocNazwyKlasKtorychUcze(Long pesel) {

    List<Klasa> klasy = zwrocKlasyKtorychUcze(pesel);

    String nazwyKlas[] = new String[klasy.size()];
    int i = 0;
    for (Klasa klasa : klasy) {
      nazwyKlas[i] = klasa.getNazwaKlasy();
      i++;
    }

    return nazwyKlas;
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

  public static List<Przedmiot> zwrocPrzedmiotyKtorychUczeDanaKlase(String klasa, Long pesel) {

    CriteriaQuery<Przedmiot> criteria = builder.createQuery(Przedmiot.class);
    Root<Zajecia> root = criteria.from(Zajecia.class);
    criteria.select(root.get("przedmiot"));
    criteria.where(builder.equal(root.get("klasa"), klasa));
    criteria.where(builder.equal(root.get("nauczyciel"), pesel));
    criteria.distinct(true);

    List<Przedmiot> przedmioty = entityManager.createQuery(criteria).getResultList();

    return przedmioty;
  }

  public static Uczen zwrocUcznia(Long pesel) {
    CriteriaQuery<Uczen> criteria = builder.createQuery(Uczen.class);
    Root<Uczen> root = criteria.from(Uczen.class);
    criteria.select(root);
    criteria.where(builder.equal(root.get("pesel"), pesel));
    Uczen uczen = entityManager.createQuery(criteria).getSingleResult();

    return uczen;

  }

  public static void zwrocMaxLiczbeOcenZdanegoPrzedmiotu() {
// dorob sprawdzanie dla klasy
    CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
    Root<Ocena> root = criteria.from(Ocena.class);
    criteria.groupBy(root.get("przedmiot"));
    //criteria.groupBy(root.get("uczen"));
    criteria.multiselect(root.get("przedmiot"), builder.count(root.get("stopien")));

    List<Tuple> tuples = entityManager.createQuery(criteria).getResultList();
    for (Tuple tuple : tuples) {
      Przedmiot przedmiotek = (Przedmiot) tuple.get(0);
      Long count = (Long) tuple.get(1);
    }
  }

  public static List<String> zwrocRodzajeOcen() {

    CriteriaQuery<String> criteria = builder.createQuery(String.class);
    Root<RodzajOceny> root = criteria.from(RodzajOceny.class);
    criteria.select(root.get("rodzajOceny"));
    List<String> rodzajeOcen = entityManager.createQuery(criteria).getResultList();

    return rodzajeOcen;
  }

  public static List<Ocena> zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu(Uczen gagatek, Przedmiot przedmiot) {

    CriteriaQuery<Ocena> criteria = builder.createQuery(Ocena.class);
    Root<Ocena> root = criteria.from(Ocena.class);
    criteria.select(root);
    criteria.where(builder.equal(root.get("uczen"), gagatek), (builder.equal(root.get("przedmiot"), przedmiot)));
    List<Ocena> rodzajeOcen = entityManager.createQuery(criteria).getResultList();
    return rodzajeOcen;

  }

  public static Long uzyskajPeselZalogowany(String login, String haslo) {
    CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
    Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
    criteria.select(root.get("pesel"));
    criteria.where(builder.equal(root.get("login"), login), builder.equal(root.get("haslo"), haslo));
    Long nr_pesel = null;
    try {
      nr_pesel = entityManager.createQuery(criteria).getSingleResult();
    } catch (NoResultException e) {
      System.out.println("nic nie pasuje");
    }
    //Long nr_pesel = entityManager.createQuery(criteria).getSingleResult();

    return nr_pesel;
  }

  public static String uzyskajKtoZalogowany(Long pesel) {
    CriteriaQuery<String> criteria = builder.createQuery(String.class);
    Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
    criteria.select(root.get("kto"));
    criteria.where(builder.equal(root.get("pesel"), pesel));
    String osoba = null;
    try {
      osoba = entityManager.createQuery(criteria).getSingleResult();
    } catch (NoResultException e) {
      System.out.println("nic nie pasuje");
    }
    // String osoba = entityManager.createQuery(criteria).getSingleResult();

    return osoba;
  }

  public static String uzyskajLoginZalogowany(Long pesel) {
    CriteriaQuery<String> criteria = builder.createQuery(String.class);
    Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
    criteria.select(root.get("login"));
    criteria.where(builder.equal(root.get("pesel"), pesel));
    String osoba = null;
    try {
      osoba = entityManager.createQuery(criteria).getSingleResult();
    } catch (NoResultException e) {
      System.out.println("nic nie pasuje");
    }
    //String osoba = entityManager.createQuery(criteria).getSingleResult();

    return osoba;
  }

  private void sprawdzDaneLogowania() {
    //trzeba przeprowadzić testy przed cryteria bo występuje wyjątek 
    //Exception in thread "main" javax.persistence.NoResultException: No entity found for query
  }

  public static void wstawOcene(Ocena ocena) {

    Session session = sessionFactory.openSession();

    Transaction tx = null;
    Integer stId = null;
    try {
      tx = session.beginTransaction();

      session.save(ocena);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
    } finally {
      session.close();
    }

  }

  public static void edytujOcene(Ocena ocena) {

    Session session = sessionFactory.openSession();

    Transaction tx = null;
    Integer stId = null;
    try {
      tx = session.beginTransaction();

      session.merge(ocena);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
    } finally {
      session.close();
    }

  }

  public static Klasa zwrocPlan(String klasa) {
    CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
    Root<Klasa> root = criteria.from(Klasa.class);
    criteria.select(root);
    criteria.where(builder.equal(root.get("nazwaKlasy"), klasa));
    Klasa plan = entityManager.createQuery(criteria).getSingleResult();

    return plan;
  }

  public static List<Obecnosc> zwrocObecnosciMoichUczniowZmojegoPrzedmiotu(Przedmiot przedmiot, Klasa klasa) {
    List<Obecnosc> obecnosci = new ArrayList<>();

    return obecnosci;
  }

  public static Nauczyciel zwrocNauczyciela(Long pesel) {

    CriteriaQuery<Nauczyciel> criteria = builder.createQuery(Nauczyciel.class);
    Root<Nauczyciel> root = criteria.from(Nauczyciel.class);
    criteria.select(root);
    criteria.where(builder.equal(root.get("pesel"), pesel));
    Nauczyciel nauczyciel = entityManager.createQuery(criteria).getSingleResult();

    return nauczyciel;
  }

  // DO SKONCZENIA do wstawiania 2 buttonow jak mam 2 zajecia w danym dniu
  public static void zwrocIleMamZajecWdanymDniu(Long pesel, Przedmiot przedmiot) {
    // DO SKONCZENIA
    //Przedmiot przedmiot = new Przedmiot("algebra_liniowa");
    CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
    Root<Zajecia> root = criteria.from(Zajecia.class);
    criteria.multiselect(root.get("godzina"), root.get("dzien"));
    criteria.where(builder.equal(root.get("nauczyciel"), zwrocNauczyciela(pesel)), (builder.equal(root.get("przedmiot"), przedmiot)));
    List<Tuple> tuples = entityManager.createQuery(criteria).getResultList();
    Set<Integer> linkedHashSet = new LinkedHashSet<>();
    for (Tuple tuple : tuples) {
      Date godzina = (Date) tuple.get(0);
      String dzien = (String) tuple.get(1);
    }

  }

  public static List<Integer> zwrocWJakieDniTygodniaMamZajecia(Long pesel, Przedmiot przedmiot) {

    CriteriaQuery<String> criteria = builder.createQuery(String.class);
    Root<Zajecia> root = criteria.from(Zajecia.class);
    criteria.select(root.get("dzien"));
    criteria.where(builder.equal(root.get("nauczyciel"), zwrocNauczyciela(pesel)), (builder.equal(root.get("przedmiot"), przedmiot)));
    criteria.distinct(true);
    List<String> kiedyZajecia = entityManager.createQuery(criteria).getResultList();
    List<Integer> zwrocDniTygodnia = new ArrayList<>();
    for (String x : kiedyZajecia) {

      if (x.equals("pon")) {
        zwrocDniTygodnia.add(1);
      } else if (x.equals("wt")) {
        zwrocDniTygodnia.add(2);
      } else if (x.equals("sr")) {
        zwrocDniTygodnia.add(3);
      } else if (x.equals("czw")) {
        zwrocDniTygodnia.add(4);
      } else if (x.equals("pt")) {
        zwrocDniTygodnia.add(5);
      }

    }
    return zwrocDniTygodnia;

  }

  public static List<Obecnosc> zwrocObecnosciZprzedmiotu(Przedmiot przedmiot, List<Uczen> uczniowie) {

    CriteriaQuery<Obecnosc> criteria = builder.createQuery(Obecnosc.class);
    Root<Obecnosc> root = criteria.from(Obecnosc.class);
    criteria.select(root);
    criteria.where(root.get("uczen").in(uczniowie));
    criteria.where(builder.equal(root.get("przedmiot"), przedmiot));
    List<Obecnosc> obecnosci = entityManager.createQuery(criteria).getResultList();
    for (Obecnosc obecnosc : obecnosci) {

    }
    return obecnosci;

  }

  public static void zwrocTuplesyObecnosciZprzedmiotu(Przedmiot przedmiot, List<Uczen> uczniowie) {


    CriteriaQuery<Obecnosc> criteria = builder.createQuery(Obecnosc.class);
    Root<Obecnosc> root = criteria.from(Obecnosc.class);
    criteria.select(root);
    criteria.where(root.get("uczen").in(uczniowie));
    criteria.where(builder.equal(root.get("przedmiot"), przedmiot));
    List<Obecnosc> obecnosci = entityManager.createQuery(criteria).getResultList();
    
    
    //return obecnosci;

  }

  public static void edytujNieobecnosc(Obecnosc obecnosc) {
    Session session = sessionFactory.openSession();

    Transaction tx = null;
    Integer stId = null;
    try {
      tx = session.beginTransaction();

      session.merge(obecnosc);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
    } finally {
      session.close();
    }

  }

  public static void dodajNieobecnosc(Obecnosc nieobecny) {
    Session session = sessionFactory.openSession();

    Transaction tx = null;
    Integer stId = null;
    try {
      tx = session.beginTransaction();

      session.save(nieobecny);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
    } finally {
      session.close();
    }
  }
    public static void usunNieobecnosc(Obecnosc nieobecny) {
    Session session = sessionFactory.openSession();

    Transaction tx = null;
    Integer stId = null;
    try {
      tx = session.beginTransaction();

      session.delete(nieobecny);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
    } finally {
      session.close();
    }
  }

  public static Obecnosc zwrocNieobecnoscZdanegoDnia(Obecnosc item, Date dataWkomorce) {

    CriteriaQuery<Obecnosc> criteria = builder.createQuery(Obecnosc.class);
    Root<Obecnosc> root = criteria.from(Obecnosc.class);
    criteria.select(root);
    criteria.where(builder.equal(root.get("uczen"), item.getUczen()), (builder.equal(root.get("przedmiot"), item.getPrzedmiot())), builder.equal(root.get("data"), dataWkomorce));

    Obecnosc obecnosc = entityManager.createQuery(criteria).getSingleResult();

    return obecnosc;
  }
  
  

}
