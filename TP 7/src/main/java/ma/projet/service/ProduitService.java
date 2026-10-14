package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
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
    public boolean update(Produit o) {
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
    public boolean delete(Produit o) {
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
    public Produit findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Produit.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Produit> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Produit", Produit.class).list();
        } finally {
            session.close();
        }
    }

    public List<Produit> findByCategorie(Categorie c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Produit> query = session.createQuery("from Produit p where p.categorie = :c", Produit.class);
            query.setParameter("c", c);
            return query.list();
        } finally {
            session.close();
        }
    }

    public List<Produit> findProduitsCommandesEntreDates(Date d1, Date d2) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Produit> query = session.createQuery(
                "select distinct l.produit from LigneCommandeProduit l where l.commande.date between :d1 and :d2",
                Produit.class
            );
            query.setParameter("d1", d1);
            query.setParameter("d2", d2);
            return query.list();
        } finally {
            session.close();
        }
    }

    public void afficherProduitsDansCommande(int commandeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Commande cmd = session.get(Commande.class, commandeId);
            if (cmd == null) {
                System.out.println("Commande introuvable : " + commandeId);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            String dateStr = sdf.format(cmd.getDate());
            if (!dateStr.isEmpty()) {
                String[] parts = dateStr.split(" ");
                if (parts.length == 3) {
                    parts[1] = Character.toUpperCase(parts[1].charAt(0)) + parts[1].substring(1);
                    dateStr = parts[0] + " " + parts[1] + " " + parts[2];
                }
            }

            System.out.println("Commande : " + cmd.getId() + "     Date : " + dateStr);
            System.out.println("Liste des produits :");
            System.out.println("Référence   Prix    Quantité");

            Query<LigneCommandeProduit> query = session.createQuery(
                "from LigneCommandeProduit l where l.commande.id = :cmdId",
                LigneCommandeProduit.class
            );
            query.setParameter("cmdId", commandeId);
            List<LigneCommandeProduit> lignes = query.list();

            for (LigneCommandeProduit l : lignes) {
                Produit p = l.getProduit();
                System.out.println(String.format("%-11s %-7s %d", p.getReference(), (int) p.getPrix() + " DH", l.getQuantite()));
            }
        } finally {
            session.close();
        }
    }

    public List<Produit> findPrixSuperieur100() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createNamedQuery("Produit.findByPrixSup100", Produit.class).list();
        } finally {
            session.close();
        }
    }
}
