package utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

        CriteriaQuery<SkladKlasy> criteria = builder.createQuery(SkladKlasy.class);
        Root<Klasa> root = criteria.from(Klasa.class);
        criteria.select(root.get("skladKlasies"));
        criteria.where(builder.equal(root.get("nazwaKlasy"), klasa));

        List<SkladKlasy> skladKlasy = entityManager.createQuery(criteria).getResultList();
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
            System.out.println(przedmiotek.getNazwaPrzedmiotu() + " count: " + count);
        }
    }

    public static List<String> zwrocRodzajeOcen() {

        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<RodzajOceny> root = criteria.from(RodzajOceny.class);
        criteria.select(root.get("rodzajOceny"));
        List<String> rodzajeOcen = entityManager.createQuery(criteria).getResultList();


        return rodzajeOcen;
    }

}
