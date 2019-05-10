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
     * @return
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
     *
     * @param pesel
     * @return
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
     *
     * @param pesel
     * @return
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
     *
     * @param pesel
     * @return
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
     *
     * @param pesel
     * @return
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
     *
     * @param klasa
     * @return
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
     *
     * @param klasa
     * @param pesel
     * @return
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
     *
     * @return
     */
    public static List<String> zwrocRodzajeOcen() {

        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<RodzajOceny> root = criteria.from(RodzajOceny.class);
        criteria.select(root.get("rodzajOceny"));
        List<String> rodzajeOcen = entityManager.createQuery(criteria).getResultList();

        return rodzajeOcen;
    }

    /**
     *
     * @param gagatek
     * @param przedmiot
     * @return
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
     *
     * @param login
     * @param haslo
     * @return
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
     *
     * @param login
     * @return
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
     *
     * @param pesel
     * @return
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
     *
     * @param pesel
     * @return
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

    private void sprawdzDaneLogowania() {
        //trzeba przeprowadzić testy przed cryteria bo występuje wyjątek 
        //Exception in thread "main" javax.persistence.NoResultException: No entity found for query
    }

    /**
     *
     * @param ocena
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
     *
     * @param ocena
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
     *
     * @param przedmiot
     * @param klasa
     * @return
     */
    public static List<Obecnosc> zwrocObecnosciMoichUczniowZmojegoPrzedmiotu(Przedmiot przedmiot, Klasa klasa) {
        List<Obecnosc> obecnosci = new ArrayList<>();

        return obecnosci;
    }

    /**
     *
     * @param pesel
     * @return
     */
    public static Nauczyciel zwrocNauczyciela(Long pesel) {

        CriteriaQuery<Nauczyciel> criteria = builder.createQuery(Nauczyciel.class);
        Root<Nauczyciel> root = criteria.from(Nauczyciel.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("pesel"), pesel));
        Nauczyciel nauczyciel = entityManager.createQuery(criteria).getSingleResult();

        return nauczyciel;
    }

    // DO SKONCZENIA do wstawiania 2 buttonow jak mam 2 zajecia w danym dniu
    /**
     *
     * @param pesel
     * @param przedmiot
     */
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

    /**
     *
     * @param pesel
     * @param przedmiot
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
     *
     * @param przedmiot
     * @param uczniowie
     * @return
     */
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
     *
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
     *
     * @param nieobecny
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
     *
     * @param nieobecny
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
     *
     * @param item
     * @param dataWkomorce
     * @return
     */
    public static Obecnosc zwrocNieobecnoscZdanegoDnia(Obecnosc item, Date dataWkomorce) {

        CriteriaQuery<Obecnosc> criteria = builder.createQuery(Obecnosc.class);
        Root<Obecnosc> root = criteria.from(Obecnosc.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("uczen"), item.getUczen()), (builder.equal(root.get("przedmiot"), item.getPrzedmiot())), builder.equal(root.get("data"), dataWkomorce));

        Obecnosc obecnosc = entityManager.createQuery(criteria).getSingleResult();

        return obecnosc;
    }

    /**
     *
     * @param pesel
     * @return
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
     *
     * @param pesel
     * @param login
     * @param haslo
     * @param kto
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
     *
     * @param pesel
     * @param imie
     * @param nazwisko
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
     *
     * @param pesel_r
     * @param pesel_uczen
     * @param imie_o
     * @param nazwisko_o
     * @param imie_m
     * @param nazwisko_m
     */
    public static void wstawRodzica(long pesel_r, long pesel_uczen, String imie_o, String nazwisko_o, String imie_m, String nazwisko_m) {
        Autoryzacja aut = zwrocAutoryzacje(pesel_r);
        Autoryzacja aut_ucz = zwrocAutoryzacje(pesel_uczen);
        Uczen uczniak = aut_ucz.getUczen();

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
     *
     * @param pesel_u
     * @param imie_u
     * @param nazwisko_u
     * @param klasa
     */
    public static void wstawUcznia(long pesel_u, long pesel_r, String imie_u, String nazwisko_u, Klasa klasa) {
        Autoryzacja aut = zwrocAutoryzacje(pesel_u);
        Autoryzacja aut_rodz = zwrocAutoryzacje(pesel_r);
        Rodzic rodzic = aut_rodz.getRodzic();
        Uczen nowy_ucz = new Uczen(aut,rodzic);
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
     *
     * @return
     */
    public static List<Klasa> pobierzKlasy() {
        CriteriaQuery<Klasa> criteria = builder.createQuery(Klasa.class);
        Root root = criteria.from(Klasa.class);
        criteria.select(root.get("nazwa_klasy"));
        List<Klasa> klasy = entityManager.createQuery(criteria).getResultList();

        return klasy;

    }

    /**
     *
     * @return
     */
    public static List<Long> pobierzListePeseli() {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root root = criteria.from(Autoryzacja.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele = entityManager.createQuery(criteria).getResultList();

        return pesele;
    }

    /**
     *
     * @return
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

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(pesele_naucz);
        pelna_lista.addAll(pesele_naucz_aut);

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
     *
     * @return
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

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(pesele_ucz);
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

    /**
     *
     * @return
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

        List<Long> pelna_lista = new ArrayList<>();
        pelna_lista.addAll(pesele_rodz);
        pelna_lista.addAll(pesele_rodz_aut);

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
     *
     * @return
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
     *
     * @return
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
     *
     * @return
     */
    public static List<Long> pobierzListePeseliRodzicow() {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root root = criteria.from(Rodzic.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele = entityManager.createQuery(criteria).getResultList();

        return pesele;
    }

    /**
     *
     * @return
     */
    public static List<Long> pobierzListePeseliNauczycieli() {
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root root = criteria.from(Nauczyciel.class);
        criteria.select(root.get("pesel"));
        List<Long> pesele = entityManager.createQuery(criteria).getResultList();

        return pesele;
    }

    /**
     *
     * @return
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

}
