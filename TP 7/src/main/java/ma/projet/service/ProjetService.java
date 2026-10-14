package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Projet.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Projet> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Projet", Projet.class).list();
        } finally {
            session.close();
        }
    }

    public void afficherTachesPlanifiees(Projet p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Tache> query = session.createQuery(
                "from Tache t where t.projet.id = :projId",
                Tache.class
            );
            query.setParameter("projId", p.getId());
            List<Tache> taches = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Tâches planifiées pour le projet " + p.getNom() + " :");
            for (Tache t : taches) {
                String d1 = t.getDateDebut() != null ? sdf.format(t.getDateDebut()) : "-";
                String d2 = t.getDateFin() != null ? sdf.format(t.getDateFin()) : "-";
                System.out.println("- " + t.getNom() + " (du " + d1 + " au " + d2 + ", prix: " + t.getPrix() + " DH)");
            }
        } finally {
            session.close();
        }
    }

    public void afficherTachesRealisees(Projet p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            SimpleDateFormat sdfFull = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            String dateDebutStr = p.getDateDebut() != null ? sdfFull.format(p.getDateDebut()) : "-";
            if (!dateDebutStr.equals("-")) {
                String[] parts = dateDebutStr.split(" ");
                if (parts.length == 3) {
                    parts[1] = Character.toUpperCase(parts[1].charAt(0)) + parts[1].substring(1);
                    dateDebutStr = parts[0] + " " + parts[1] + " " + parts[2];
                }
            }

            System.out.println("Projet : " + p.getId() + "      Nom : " + p.getNom() + "     Date début : " + dateDebutStr);
            System.out.println("Liste des tâches:");
            System.out.println("Num Nom            Date Début Réelle   Date Fin Réelle");

            Query<EmployeTache> query = session.createQuery(
                "from EmployeTache et where et.tache.projet.id = :projId order by et.tache.id",
                EmployeTache.class
            );
            query.setParameter("projId", p.getId());
            List<EmployeTache> list = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (EmployeTache et : list) {
                String d1 = et.getDateDebutReelle() != null ? sdf.format(et.getDateDebutReelle()) : "-";
                String d2 = et.getDateFinReelle() != null ? sdf.format(et.getDateFinReelle()) : "-";
                System.out.println(String.format("%-3d %-14s %-19s %s", et.getTache().getId(), et.getTache().getNom(), d1, d2));
            }
        } finally {
            session.close();
        }
    }
}
