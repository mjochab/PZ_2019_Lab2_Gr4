package utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import mapping.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.entity.AbstractEntityPersister;

/**
 *
 * @author Krystian
 */
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

    /**
     *
     * @return zwraca zmienna klasowa sessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Metoda zwracająca nazwy przedmiotów z bazy danych.
     *
     * @return Tablice typu String
     */
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

    /**
     * Metoda zwracająca rolę użytkownika czyli wartość z kolumny Kto w tabeli
     * Autoryzacja.
     *
     * @param pesel - pesel użytkownika typu long
     * @return wartość z kolumny Kto dla podanego peselu
     */
    public static String zwrocKtoZalogowany(Long pesel) {

        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("kto"));
        criteria.where(builder.equal(root.get("pesel"), pesel));

        String kto = entityManager.createQuery(criteria).getSingleResult();
        return kto;

    }

    /**
     * Metoda zwraca klase którą wychowuje nauczyciel o danym peselu.
     *
     * @param pesel long, pesel nauczyciela
     * @return string z nazwa klasy np '1a'
     */
    public static String zwrocKlaseKtoraWychowuje(Long pesel) {
        CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
        Root<Nauczyciel> root = criteria.from(Nauczyciel.class);
        criteria.select(root.get("klasas"));
        criteria.where(builder.equal(root.get("pesel"), pesel));

        Klasa klasaObj = entityManager.createQuery(criteria).getSingleResult();

        String klasa = klasaObj.getNazwaKlasy();
        return klasa;
    }

    /**
     * Metoda zwraca klasy ktorych uczy dany nauczyciel (dowolny przedmiot)
     *
     * @param pesel long, pesel nauczyciela
     * @return lista obiektow typu Klasa
     */
    public static List<Klasa> zwrocKlasyKtorychUcze(Long pesel) {

        CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
        Root<Zajecia> root = criteria.from(Zajecia.class);
        criteria.select(root.get("klasa"));
        criteria.where(builder.equal(root.get("nauczyciel"), pesel));
        criteria.distinct(true);
        List<Klasa> klasy = entityManager.createQuery(criteria).getResultList();

        return klasy;

    }

    /**
     * Metoda zwraca tablice stringów z nazwami klas których uczy dany
     * nauczyciel
     *
     * @param pesel long, pesel nauczyciela
     * @return tablica stringów
     */
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

    /**
     * Metoda zwraca uczniów z konkretnej klasy
     *
     * @param klasa string z nazwą klasy np '1a'
     * @return array lista typu uczen
     */
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

    /**
     * zwraca przedmioty ktorych uczy dany nauczyciel
     *
     * @param klasa string z nazwa klasy
     * @param pesel long, pesel nauczyciela
     * @return lista z obiektami typu przedmiot
     */
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

    /**
     * Metoda zwracająca z bazy danych ucznia, weryfikująca po peselu.
     *
     * @param pesel - long, pesel ucznia
     * @return Obiekt typu Uczenu.
     */
    public static Uczen zwrocUcznia(Long pesel) {
        CriteriaQuery<Uczen> criteria = builder.createQuery(Uczen.class);
        Root<Uczen> root = criteria.from(Uczen.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("pesel"), pesel));
        Uczen uczen = entityManager.createQuery(criteria).getSingleResult();

        return uczen;

    }

    /**
     * Metoda zwraca rodzaje dostępnych ocen z bazy
     *
     * @return lista stringów
     */
    public static List<String> zwrocRodzajeOcen() {

        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<RodzajOceny> root = criteria.from(RodzajOceny.class);
        criteria.select(root.get("rodzajOceny"));
        List<String> rodzajeOcen = entityManager.createQuery(criteria).getResultList();

        return rodzajeOcen;
    }

    /**
     * Metoda zwraca oceny konkretnego ucznia z konkretnego przedmiotu
     *
     * @param gagatek obiekt Uczen
     * @param przedmiot obiekt Przedmiot
     * @return lista z obiektami typu Ocena
     */
    public static List<Ocena> zwrocObiektyOcenyGagatkaZmojegoPrzedmiotu(Uczen gagatek, Przedmiot przedmiot) {

        CriteriaQuery<Ocena> criteria = builder.createQuery(Ocena.class);
        Root<Ocena> root = criteria.from(Ocena.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("uczen"), gagatek), (builder.equal(root.get("przedmiot"), przedmiot)));
        List<Ocena> rodzajeOcen = entityManager.createQuery(criteria).getResultList();
        return rodzajeOcen;

    }

    /**
     * Metoda zwracająca numer pesel zalogowanego użytkownika po podaniu
     * parametrów login, haslo.
     *
     * @param login - login użytkownika typu String
     * @param haslo - hasło użytkownika typu String
     * @return zwraca numer pesel zalogowanego użytkownika
     */
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

    /**
     * Metoda zwraca numer pesel uzytkownika o podanym loginie.
     *
     * @param login - login użytkownika typu String
     * @return numer pesel użytkownika
     */
    public static Long uzyskajPesel(String login) {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("pesel"));
        criteria.where(builder.equal(root.get("login"), login));
        Long nr_pesel = null;
        try {
            nr_pesel = entityManager.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            //System.out.println("nic nie pasuje");
        }

        return nr_pesel;
    }

    /**
     * Metoda zwracająca rolę użytkownika, czyli wartość z kolumny Kto w tabeli
     * Autoryzacja.
     *
     * @param pesel - pesel użytkownika typu long
     * @return wartość z kolumny Kto dla podanego peselu
     */
    public static String uzyskajKtoZalogowany(Long pesel) {
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("kto"));
        criteria.where(builder.equal(root.get("pesel"), pesel));
        String osoba = null;
        try {
            osoba = entityManager.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            //System.out.println("nic nie pasuje");
        }
        // String osoba = entityManager.createQuery(criteria).getSingleResult();

        return osoba;
    }

    /**
     * Metoda zwracająca login użytkownika przypisany do numeru pesel z tabeli
     * Autoryzacja.
     *
     * @param pesel - pesel użytkownika typu long
     * @return login dla podanego peselu
     */
    public static String uzyskajLoginZalogowany(Long pesel) {
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("login"));
        criteria.where(builder.equal(root.get("pesel"), pesel));
        String osoba = null;
        try {
            osoba = entityManager.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            //System.out.println("nic nie pasuje");
        }
        //String osoba = entityManager.createQuery(criteria).getSingleResult();

        return osoba;
    }

    /**
     * Metoda wstawia ocene do bazy
     *
     * @param ocena obiekt ocena
     */
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

    /**
     * Metoda edytuje ocene z bazy
     *
     * @param ocena obiekt ocena
     */
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

    /**
     * Metoda zwracająca plan zajęć danej klasy.
     *
     * @param klasa - String, klasa której plan zostanie zwrócony.
     * @return obiekt Klasa zawierający plan.
     */
    public static Klasa zwrocPlan(String klasa) {
        CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
        Root<Klasa> root = criteria.from(Klasa.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("nazwaKlasy"), klasa));
        Klasa plan = entityManager.createQuery(criteria).getSingleResult();

        return plan;
    }

    /**
     * Metoda współdziałająca w kolejnej metodzie: zwrocIleMamZajecWdanymDniu.
     * Sluzy do zwracania nauczyciela po peselu
     *
     * @param pesel long, pesel nauczyciela
     * @return obiekt Nauczyciel
     */
    public static Nauczyciel zwrocNauczyciela(Long pesel) {

        CriteriaQuery<Nauczyciel> criteria = builder.createQuery(Nauczyciel.class);
        Root<Nauczyciel> root = criteria.from(Nauczyciel.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("pesel"), pesel));
        Nauczyciel nauczyciel = entityManager.createQuery(criteria).getSingleResult();

        return nauczyciel;
    }

    /**
     * Metoda testowa, oblicza ile dany nauczyciel ma zajęć z danego przedmiotu
     * w danym dniu
     *
     * @param pesel long, pesel nauczyciela
     * @param przedmiot obiekt Przedmiot
     */
    public static void zwrocIleMamZajecWdanymDniu(Long pesel, Przedmiot przedmiot) {
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

    /**
     * Metoda zwraca liste integerow w ktorych dniach tygodnia ma zajecia dany
     * nauczyciel, np. 1 - poniedzialek, 2 - wtorek itd
     *
     * @param pesel long, pesel nauczyciela
     * @param przedmiot obiekt Przedmiot
     * @return
     */
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

    /**
     * Metoda zwracająca listę nieobecności danego ucznia.
     *
     * @param uczen - typ Uczen, obiekt
     * @return List typu Obecnosc.
     */
    public static List<Obecnosc> zwrocNieobecnosciUcznia(Uczen uczen) {

        CriteriaQuery<Obecnosc> criteria = builder.createQuery(Obecnosc.class);
        Root<Obecnosc> root = criteria.from(Obecnosc.class);
        Predicate predicate = builder.equal(root.get("wartosc"), "o");
        criteria.select(root);
        criteria.where(builder.equal(root.get("uczen"), uczen), (predicate.not()));

        List<Obecnosc> nieobecnosci = entityManager.createQuery(criteria).getResultList();

        return nieobecnosci;

    }

    /**
     * Metoda edytuje nieobecnosc w bazie
     *
     * @param obecnosc obiekt typu Obecnosc
     */
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

    /**
     * Metoda dodaje nieobecnosc do bazy
     *
     * @param nieobecny obiekt Obecnosc
     */
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

    /**
     * Metoda usuwa daną nieobecnosc z bazy
     *
     * @param nieobecny obiekt Obecnosc
     */
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

    /**
     * Metoda usprawiedliwiająća nieobecność.
     *
     * @param obecnosc - typ Obecnosc, nieobecność do usprawiedliwienia
     */
    public static void usprawiedliwNieobecnosc(Obecnosc obecnosc) {
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

    /**
     * Metoda, w której po podaniu numeru pesel użytkownika jako argumentu
     * metody zwracany jest obiekt autoryzacji użytkownika z klasy Autoryzacja.
     *
     * @param pesel - numer pesel typu long
     * @return obiekt Autoryzacja
     */
    public static Autoryzacja zwrocAutoryzacje(Long pesel) {
        CriteriaQuery<Autoryzacja> criteria = builder.createQuery(Autoryzacja.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("pesel"), pesel));
        Autoryzacja aut = entityManager.createQuery(criteria).getSingleResult();

        return aut;
    }

    /**
     * Metoda tworzy nowy obiekt Autoryzacja w bazie danych o padanych
     * parametrach.
     *
     * @param pesel - numer pesel typu long
     * @param login - login użytkownika typu String
     * @param haslo - hasło użytkownika typu String
     * @param kto - oznaczenie roli użytkownika typu String
     */
    public static void wstawAutoryzacje(long pesel, String login, String haslo, String kto) {
        Autoryzacja nowa_os = new Autoryzacja(pesel, login, haslo, kto);
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();

            session.save(nowa_os);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda dodaje nowy obiekt Nauczyciel o podanych parametrach. Numer pesel
     * niezbędny jest by przypisać Autoryzację do nowego obiektu.
     *
     * @param pesel - numer pesel typu long
     * @param imie - imie nauczyciela typu String
     * @param nazwisko - nazwisko nauczyciela typu String
     */
    public static void wstawNauczyciela(long pesel, String imie, String nazwisko) {
        Autoryzacja aut = zwrocAutoryzacje(pesel);
        Nauczyciel nowa_os = new Nauczyciel();
        nowa_os.setImie(imie);
        nowa_os.setNazwisko(nazwisko);
        nowa_os.setAutoryzacja(aut);
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.save(nowa_os);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda dodaje nowy obiekt Rodzic o podanych parametrach. Numer pesel
     * niezbędny jest by przypisać Autoryzację do nowego obiektu.
     *
     * @param pesel_r - pesel rodzica typu long
     * @param imie_o - imię ojca typu String
     * @param nazwisko_o - nazwisko ojca typu String
     * @param imie_m - imię matki typu String
     * @param nazwisko_m - nazwisko matki typu String
     */
    public static void wstawRodzica(long pesel_r, String imie_o, String nazwisko_o, String imie_m, String nazwisko_m) {
        Autoryzacja aut = zwrocAutoryzacje(pesel_r);

        Rodzic nowy_rodzic = new Rodzic(aut);
        nowy_rodzic.setImieMatki(imie_m);
        nowy_rodzic.setImieOjca(imie_o);
        nowy_rodzic.setNazwiskoMatki(nazwisko_m);
        nowy_rodzic.setNazwiskoOjca(nazwisko_o);

        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.save(nowy_rodzic);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda dodaje nowy obiekt Uczen o podanych parametrach. Numer pesel
     * ucznia niezbędny jest by przypisać Autoryzację do nowego obiektu, a numer
     * pesel rodzica wymagany jest by przypisać uczniowi obiekt rodzica.
     *
     * @param pesel_u - pesel ucznia typu long
     * @param pesel_r - pesel rodzica typu long
     * @param imie_u - imię ucznia typu String
     * @param nazwisko_u - nazwisko ucznia typu String
     * @param klasa - klasa ucznia typu Klasa
     */
    public static void wstawUcznia(long pesel_u, long pesel_r, String imie_u, String nazwisko_u, Klasa klasa) {
        Autoryzacja aut = zwrocAutoryzacje(pesel_u);
        Autoryzacja aut_rodz = zwrocAutoryzacje(pesel_r);
        Rodzic rodzic = aut_rodz.getRodzic();
        Uczen nowy_ucz = new Uczen(aut, rodzic);
        nowy_ucz.setImie(imie_u);
        nowy_ucz.setNazwisko(nazwisko_u);
        nowy_ucz.setKlasa(klasa);

        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.save(nowy_ucz);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda wykorzystywana by pobrać nazwy istniejących klas w bazie i zwrócić
     * w postaci listy typu String.
     *
     * @return lista typu String
     */
    public static List<String> pobierzKlasy() {
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root root = criteria.from(Klasa.class);
        criteria.select(root.get("nazwaKlasy"));
        List<String> klasy = entityManager.createQuery(criteria).getResultList();

        return klasy;

    }

    /**
     * Metoda wykorzystywana by pobrać pesele istniejące w tabeli autoryzacji w
     * bazie i zwrócić w postaci listy typu Long.
     *
     * @return lista typu Long
     */
    public static List<Long> pobierzListePeseli() {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele = entityManager.createQuery(criteria).getResultList();

        return pesele;
    }

    /**
     * Metoda wykorzystywana by pobrać pesele, które są w tabeli Autoryzacja w
     * bazie danych oznaczone jako należące do nauczycieli i są aktywne, czyli w
     * kolumnie Kto mają oznaczenie "n" oraz znalezione pesele nie są jeszcze
     * dodane do tabeli Nauczyciel.
     *
     * @return lista typu Long
     */
    public static List<Long> podajPeseleNauczycielaBezDanych() {
        List<Long> peselki = new ArrayList<>();

        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("pesel"));
        criteria.where(builder.equal(root.get("kto"), "n"));
        List<Long> pesele_naucz_aut = entityManager.createQuery(criteria).getResultList();

        CriteriaQuery<Long> criteria2 = builder.createQuery(Long.class);
        Root<Nauczyciel> root2 = criteria2.from(Nauczyciel.class);
        criteria2.select(root2.get("pesel"));
        List<Long> pesele_naucz = entityManager.createQuery(criteria2).getResultList();

        CriteriaQuery<Long> criteria3 = builder.createQuery(Long.class);
        Root<Autoryzacja> root3 = criteria3.from(Autoryzacja.class);
        criteria3.select(root3.get("pesel"));
        criteria3.where(builder.equal(root3.get("kto"), "b"));
        List<Long> pesele_rodz_aut2 = entityManager.createQuery(criteria3).getResultList();

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(pesele_naucz);
        pelna_lista.addAll(pesele_naucz_aut);
        pelna_lista.addAll(pesele_rodz_aut2);

        for (int i = 0; i < pelna_lista.size(); i++) {
            boolean uniq = true;
            for (int j = 0; j < pelna_lista.size(); j++) {
                if (i != j) {
                    if (Objects.equals(pelna_lista.get(i), pelna_lista.get(j))) {
                        uniq = false;
                    }
                }
            }
            if (uniq) {
                //System.out.println(pelna_lista.get(i));
                peselki.add(pelna_lista.get(i));
            }
        }
        return peselki;
    }

    /**
     * Metoda wykorzystywana by pobrać pesele, które są w tabeli Autoryzacja w
     * bazie danych oznaczone jako należące do uczniów i są aktywne, czyli w
     * kolumnie Kto mają oznaczenie "u" oraz znalezione pesele nie są jeszcze
     * dodane do tabeli Uczen.
     *
     * @return lista typu Long
     */
    public static List<Long> podajPeseleUczniaBezDanych() {
        List<Long> peselki = new ArrayList<>();

        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("pesel"));
        criteria.where(builder.equal(root.get("kto"), "u"));
        List<Long> pesele_ucz_aut = entityManager.createQuery(criteria).getResultList();

        CriteriaQuery<Long> criteria2 = builder.createQuery(Long.class);
        Root<Uczen> root2 = criteria2.from(Uczen.class);
        criteria2.select(root2.get("pesel"));
        List<Long> pesele_ucz = entityManager.createQuery(criteria2).getResultList();

        CriteriaQuery<Long> criteria3 = builder.createQuery(Long.class);
        Root<Autoryzacja> root3 = criteria3.from(Autoryzacja.class);
        criteria3.select(root3.get("pesel"));
        criteria3.where(builder.equal(root3.get("kto"), "a"));
        List<Long> pesele_rodz_aut2 = entityManager.createQuery(criteria3).getResultList();

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(pesele_ucz);
        pelna_lista.addAll(pesele_ucz_aut);
        pelna_lista.addAll(pesele_rodz_aut2);

        for (int i = 0; i < pelna_lista.size(); i++) {
            boolean uniq = true;
            for (int j = 0; j < pelna_lista.size(); j++) {
                if (i != j) {
                    if (Objects.equals(pelna_lista.get(i), pelna_lista.get(j))) {
                        uniq = false;
                    }
                }
            }
            if (uniq) {
                //System.out.println(pelna_lista.get(i));
                peselki.add(pelna_lista.get(i));
            }
        }
        return peselki;
    }

    /**
     * Metoda wykorzystywana by pobrać pesele, które są w tabeli Autoryzacja w
     * bazie danych oznaczone jako należące do rodziców i są aktywne, czyli w
     * kolumnie Kto mają oznaczenie "r" oraz znalezione pesele nie są jeszcze
     * dodane do tabeli Rodzic.
     *
     * @return lista typu Long
     */
    public static List<Long> podajPeseleRodzicaBezDanych() {
        List<Long> peselki = new ArrayList<>();

        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Autoryzacja> root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("pesel"));
        criteria.where(builder.equal(root.get("kto"), "r"));
        List<Long> pesele_rodz_aut = entityManager.createQuery(criteria).getResultList();

        CriteriaQuery<Long> criteria2 = builder.createQuery(Long.class);
        Root<Rodzic> root2 = criteria2.from(Rodzic.class);
        criteria2.select(root2.get("pesel"));
        List<Long> pesele_rodz = entityManager.createQuery(criteria2).getResultList();

        CriteriaQuery<Long> criteria3 = builder.createQuery(Long.class);
        Root<Autoryzacja> root3 = criteria3.from(Autoryzacja.class);
        criteria3.select(root3.get("pesel"));
        criteria3.where(builder.equal(root3.get("kto"), "c"));
        List<Long> pesele_rodz_aut2 = entityManager.createQuery(criteria3).getResultList();

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(pesele_rodz);
        pelna_lista.addAll(pesele_rodz_aut);
        pelna_lista.addAll(pesele_rodz_aut2);

        for (int i = 0; i < pelna_lista.size(); i++) {
            boolean uniq = true;
            for (int j = 0; j < pelna_lista.size(); j++) {
                if (i != j) {
                    if (Objects.equals(pelna_lista.get(i), pelna_lista.get(j))) {
                        uniq = false;
                    }
                }
            }
            if (uniq) {
                //System.out.println(pelna_lista.get(i));
                peselki.add(pelna_lista.get(i));
            }
        }
        return peselki;
    }

    /**
     * Metoda wykorzystywana by pobrać pesele, które są w tabeli Uczen, ale nie
     * mają jeszcze przypisanego obiektu rodzica.
     *
     * @return lista typu Long
     */
    public static List<Long> podajPeseleUczniaBezRodzica() {
        List<Long> peselki = new ArrayList<>();

        List<Long> pesele_rodz_dz = pobierzListePeseliUczniowZRodzica();

        CriteriaQuery<Long> criteria2 = builder.createQuery(Long.class);
        Root<Uczen> root2 = criteria2.from(Uczen.class);
        criteria2.select(root2.get("pesel"));
        List<Long> pesele_ucz = entityManager.createQuery(criteria2).getResultList();

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(pesele_ucz);
        pelna_lista.addAll(pesele_rodz_dz);

        for (int i = 0; i < pelna_lista.size(); i++) {
            boolean uniq = true;
            for (int j = 0; j < pelna_lista.size(); j++) {
                if (i != j) {
                    if (Objects.equals(pelna_lista.get(i), pelna_lista.get(j))) {
                        uniq = false;
                    }
                }
            }
            if (uniq) {
                //System.out.println(pelna_lista.get(i));
                peselki.add(pelna_lista.get(i));
            }
        }
        return peselki;
    }

    /**
     * Metoda wykorzystywana w metodzie podajPeseleUczniaBezRodzica() by wypisać
     * listę peseli uczniów bez przypisanego obiektu rodzica.
     *
     * @see podajPeseleUczniaBezRodzica
     * @return lista typu Long
     */
    public static List<Long> pobierzListePeseliUczniowZRodzica() {
        CriteriaQuery<Uczen> criteria = builder.createQuery(Uczen.class);
        Root root = criteria.from(Rodzic.class);
        criteria.select(root.get("uczen"));
        List<Uczen> pesele_rodz_dz = entityManager.createQuery(criteria).getResultList();

        List<Long> peselki = new ArrayList<>();
        for (int i = 0; i < pesele_rodz_dz.size(); i++) {
            peselki.add(pesele_rodz_dz.get(i).getPesel());
        }
        return peselki;
    }

    /**
     * Metoda wypisująca liste peseli rodziców z tabeli Rodzic w bazie danych.
     *
     * @return lista typu Long
     */
    public static List<Long> pobierzListePeseliRodzicow() {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root root = criteria.from(Rodzic.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele = entityManager.createQuery(criteria).getResultList();

        return pesele;
    }

    /**
     * Metoda wypisująca liste peseli rodziców z tabeli Nauczyciel w bazie
     * danych.
     *
     * @return lista typu Long
     */
    public static List<Long> pobierzListePeseliNauczycieli() {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root root = criteria.from(Nauczyciel.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele = entityManager.createQuery(criteria).getResultList();

        return pesele;
    }

    /**
     * Metoda wypisująca liste peseli rodziców z tabeli Uczen w bazie danych.
     *
     * @return lista typu Long
     */
    public static List<Long> pobierzListePeseliUczniow() {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root root = criteria.from(Uczen.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele = entityManager.createQuery(criteria).getResultList();

        return pesele;
    }

    /**
     * Metoda zwracająca z bazdy danych rodzica, weryfikująca po peselu.
     *
     * @param pesel - typ Long, pesel rodzica.
     * @return obiekt typu Rodzic
     */
    public static Rodzic zwrocRodzica(Long pesel) {
        CriteriaQuery<Rodzic> criteria = builder.createQuery(Rodzic.class);
        Root<Rodzic> root = criteria.from(Rodzic.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("pesel"), pesel));
        Rodzic rodzic = entityManager.createQuery(criteria).getSingleResult();

        return rodzic;
    }

    /**
     * Metoda służąca do edycji danych wybranego rodzica weryfikowanego po
     * numerze pesel.
     *
     * @param pesel_r - pesel rodzica typu long
     * @param imie_o - imię ojca typu String
     * @param nazwisko_o - nazwisko ojca typu String
     * @param imie_m - imie matki typu String
     * @param nazwisko_m - nazwisko matki typu String
     */
    public static void edytujRodzicaNoweDane(long pesel_r, String imie_o, String nazwisko_o, String imie_m, String nazwisko_m) {
        Rodzic nowy_rodzic = zwrocRodzica(pesel_r);
        nowy_rodzic.setImieMatki(imie_m);
        nowy_rodzic.setImieOjca(imie_o);
        nowy_rodzic.setNazwiskoMatki(nazwisko_m);
        nowy_rodzic.setNazwiskoOjca(nazwisko_o);

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.merge(nowy_rodzic);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda służąca do edycji danych wybranego ucznia weryfikowanego po
     * numerze pesel wraz z możliwą zmianą przypisanego rodzica.
     *
     * @param pesel_u - pesel ucznia typu long
     * @param pesel_r - pesel rodzica typu long
     * @param imie_u - imię ucznia typu String
     * @param nazwisko_u - nazwisko ucznia typu String
     * @param klasa - klasa ucznia typu Klasa
     */
    public static void edytujUczniaNoweDane(long pesel_u, long pesel_r, String imie_u, String nazwisko_u, Klasa klasa) {
        Uczen uczen_nowy = zwrocUcznia(pesel_u);
        Rodzic rodzic = zwrocRodzica(pesel_r);
        uczen_nowy.setRodzic(rodzic);
        uczen_nowy.setImie(imie_u);
        uczen_nowy.setNazwisko(nazwisko_u);
        uczen_nowy.setKlasa(klasa);

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.merge(uczen_nowy);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda służąca do edycji danych wybranego nauczyciela weryfikowanego po
     * numerze pesel.
     *
     * @param pesel - pesel nauczyciela typu long
     * @param imie - imię nauczyciela typu String
     * @param nazwisko - nazwisko nauczyciela typu String
     */
    public static void edytujNauczyciela(long pesel, String imie, String nazwisko) {
        Nauczyciel nowa_os = zwrocNauczyciela(pesel);
        nowa_os.setImie(imie);
        nowa_os.setNazwisko(nazwisko);

        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.merge(nowa_os);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda usuwająca dostęp uczniowi do logowania się do aplikacji poprzez
     * zmienienie jego roli w kolumnie Kto w tabeli Autoryzacja.
     *
     * @param pesel - pesel ucznia typu long
     */
    public static void usunAutoryzacjeUcznia(long pesel) {
        Autoryzacja stara_aut = zwrocAutoryzacje(pesel);
        stara_aut.setKto("a");
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.merge(stara_aut);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda usuwająca dostęp nauczycielowi do logowania się do aplikacji
     * poprzez zmienienie jego roli w kolumnie Kto w tabeli Autoryzacja.
     *
     * @param pesel - pesel nauczyciela typu long
     */
    public static void usunAutoryzacjeNauczyciela(long pesel) {
        Autoryzacja stara_aut = zwrocAutoryzacje(pesel);
        stara_aut.setKto("b");
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.merge(stara_aut);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Metoda usuwająca dostęp rodzicowi do logowania się do aplikacji poprzez
     * zmienienie jego roli w kolumnie Kto w tabeli Autoryzacja.
     *
     * @param pesel - pesel rodzica typu long
     */
    public static void usunAutoryzacjeRodzica(long pesel) {
        Autoryzacja stara_aut = zwrocAutoryzacje(pesel);
        stara_aut.setKto("c");
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.merge(stara_aut);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static void dodajNowaKlase(Klasa klasa) {
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();

            session.save(klasa);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

    }

    /**
     * @return lista typu Long
     */
    public static List<Long> podajPeseleNauczycielaBezWychowankow() {
        List<Long> peselki = new ArrayList<>();

        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Nauczyciel> root = criteria.from(Nauczyciel.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele_ucz_aut = entityManager.createQuery(criteria).getResultList();

        CriteriaQuery<Nauczyciel> criteria2 = builder.createQuery(Nauczyciel.class);
        Root<Klasa> root2 = criteria2.from(Klasa.class);
        criteria2.select(root2.get("nauczyciel"));
        List<Nauczyciel> pesele_ucz = entityManager.createQuery(criteria2).getResultList();

        List<Long> nauczyciele_pesele = new ArrayList<>();
        for (int i = 0; i < pesele_ucz.size(); i++) {
            nauczyciele_pesele.add(pesele_ucz.get(i).getPesel());
        }

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(nauczyciele_pesele);
        pelna_lista.addAll(pesele_ucz_aut);

        for (int i = 0; i < pelna_lista.size(); i++) {
            boolean uniq = true;
            for (int j = 0; j < pelna_lista.size(); j++) {
                if (i != j) {
                    if (Objects.equals(pelna_lista.get(i), pelna_lista.get(j))) {
                        uniq = false;
                    }
                }
            }
            if (uniq) {
                //System.out.println(pelna_lista.get(i));
                peselki.add(pelna_lista.get(i));
            }
        }
        return peselki;
    }

    public static Klasa pobierzKlasePoNazwie(String nazwaKlasy) {
        CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
        Root root = criteria.from(Klasa.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("nazwaKlasy"), nazwaKlasy));
        Klasa klasa = entityManager.createQuery(criteria).getSingleResult();

        return klasa;
    }

    public static void edytujKlaseZNowymiDanymi(Klasa klasa) {
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.merge(klasa);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static Boolean sprawdzCzyKlasaJestPowiazana(Klasa klasa) {
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root root = criteria.from(SkladKlasy.class);
        criteria.select(root.get("klasa"));
        List<String> klasySklad = entityManager.createQuery(criteria).getResultList();
        CriteriaQuery<String> criteria2 = builder.createQuery(String.class);
        Root root2 = criteria2.from(Zajecia.class);
        criteria2.select(root2.get("klasa"));
        List<String> zajeciaKlasy = entityManager.createQuery(criteria2).getResultList();
        List<String> klasy = new ArrayList<>();
        klasy.addAll(klasySklad);
        klasy.addAll(zajeciaKlasy);

        return klasy.contains(klasa);
    }

    public static void usunKlaseJesliNieMaPowiazan(Klasa klasa) {
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.delete(klasa);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static void dodajNowyPrzedmiot(Przedmiot przedmiot) {
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.save(przedmiot);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }
    
        public static void usunPrzedmiot(Przedmiot przedmiot) {
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        Integer stId = null;
        try {
            tx = session.beginTransaction();
            session.delete(przedmiot);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static Przedmiot pobierzPrzedmiotPoNazwie(String nazwa) {
        CriteriaQuery<Przedmiot> criteria = builder.createQuery(Przedmiot.class);
        Root root = criteria.from(Przedmiot.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("nazwaPrzedmiotu"), nazwa));
        Przedmiot prz = entityManager.createQuery(criteria).getSingleResult();

        return prz;
    }

    public static Boolean sprawdzCzyPrzedmiotJestPowiazany(Przedmiot przedmiot) {
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root root = criteria.from(Ocena.class);
        criteria.select(root.get("przedmiot"));
        List<String> przedmiotOcena = entityManager.createQuery(criteria).getResultList();

        CriteriaQuery<String> criteria2 = builder.createQuery(String.class);
        Root root2 = criteria2.from(Zajecia.class);
        criteria2.select(root2.get("przedmiot"));
        List<String> przedmiotZajecia = entityManager.createQuery(criteria2).getResultList();

        CriteriaQuery<String> criteria3 = builder.createQuery(String.class);
        Root root3 = criteria3.from(Obecnosc.class);
        criteria3.select(root3.get("przedmiot"));
        List<String> przedmiotObecnosc = entityManager.createQuery(criteria3).getResultList();

        List<String> klasy = new ArrayList<>();
        klasy.addAll(przedmiotOcena);
        klasy.addAll(przedmiotZajecia);
        klasy.addAll(przedmiotObecnosc);

        return klasy.contains(przedmiot);
    }
    
        public static List<String> pobierzListePrzedmiotow() {
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root root = criteria.from(Przedmiot.class);
        criteria.select(root.get("nazwaPrzedmiotu"));
        List<String> prz = entityManager.createQuery(criteria).getResultList();

        return prz;
    }
}

