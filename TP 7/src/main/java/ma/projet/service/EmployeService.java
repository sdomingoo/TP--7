package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.List;

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public Employe findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Employe.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Employe> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Employe", Employe.class).list();
        } finally {
            session.close();
        }
    }

    public void afficherTachesRealisees(Employe e) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<EmployeTache> query = session.createQuery(
                "from EmployeTache et where et.employe.id = :empId",
                EmployeTache.class
            );
            query.setParameter("empId", e.getId());
            List<EmployeTache> list = query.list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Tâches réalisées par l'employé " + e.getNom() + " " + e.getPrenom() + " :");
            for (EmployeTache et : list) {
                String d1 = et.getDateDebutReelle() != null ? sdf.format(et.getDateDebutReelle()) : "-";
                String d2 = et.getDateFinReelle() != null ? sdf.format(et.getDateFinReelle()) : "-";
                System.out.println("- " + et.getTache().getNom() + " (du " + d1 + " au " + d2 + ")");
            }
        } finally {
            session.close();
        }
    }

    public void afficherProjetsGeres(Employe e) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Projet> query = session.createQuery(
                "from Projet p where p.chefProjet.id = :empId",
                Projet.class
            );
            query.setParameter("empId", e.getId());
            List<Projet> list = query.list();

            System.out.println("Projets gérés par " + e.getNom() + " " + e.getPrenom() + " :");
            for (Projet p : list) {
                System.out.println("- " + p.getNom() + " (ID: " + p.getId() + ")");
            }
        } finally {
            session.close();
        }
    }
}
