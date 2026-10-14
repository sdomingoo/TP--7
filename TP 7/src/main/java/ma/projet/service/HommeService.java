package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Homme o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Homme o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Homme findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Homme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Homme> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Homme", Homme.class).list();
        } finally {
            session.close();
        }
    }

    public List<Femme> findEpousesEntreDates(Homme h, Date d1, Date d2) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Femme> query = session.createQuery(
                "select distinct m.femme from Mariage m where m.homme.id = :hommeId and m.pk.dateDebut between :d1 and :d2",
                Femme.class
            );
            query.setParameter("hommeId", h.getId());
            query.setParameter("d1", d1);
            query.setParameter("d2", d2);
            return query.list();
        } finally {
            session.close();
        }
    }

    public void afficherMariages(Homme h) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Mariage> query = session.createQuery(
                "from Mariage m where m.homme.id = :hommeId order by m.pk.dateDebut asc",
                Mariage.class
            );
            query.setParameter("hommeId", h.getId());
            List<Mariage> mariages = query.list();

            List<Mariage> enCours = new ArrayList<>();
            List<Mariage> echoues = new ArrayList<>();

            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    enCours.add(m);
                } else {
                    echoues.add(m);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("Nom : " + h.getNom() + " " + h.getPrenom());
            System.out.println("Mariages En Cours :");
            int i = 1;
            for (Mariage m : enCours) {
                String d1 = m.getDateDebut() != null ? sdf.format(m.getDateDebut()) : "-";
                System.out.println(i + ". Femme : " + m.getFemme().getNom() + " " + m.getFemme().getPrenom()
                        + "   Date Début : " + d1 + "    Nbr Enfants : " + m.getNbrEnfants());
                i++;
            }

            System.out.println("\nMariages échoués :");
            int j = 1;
            for (Mariage m : echoues) {
                String d1 = m.getDateDebut() != null ? sdf.format(m.getDateDebut()) : "-";
                String d2 = m.getDateFin() != null ? sdf.format(m.getDateFin()) : "-";
                System.out.println(j + ". Femme : " + m.getFemme().getNom() + " " + m.getFemme().getPrenom()
                        + "  Date Début : " + d1);
                System.out.println("Date Fin : " + d2 + "    Nbr Enfants : " + m.getNbrEnfants());
                j++;
            }
        } finally {
            session.close();
        }
    }
}
