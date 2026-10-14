package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class App {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            System.out.println("==================================================");
            System.out.println("       PARTIE 1 : GESTION DE STOCK");
            System.out.println("==================================================");

            CategorieService cs = new CategorieService();
            ProduitService ps = new ProduitService();
            CommandeService cmds = new CommandeService();
            LigneCommandeService lcs = new LigneCommandeService();

            Categorie catInfo = new Categorie("INF", "Informatique");
            Categorie catAcc = new Categorie("ACC", "Accessoires");
            cs.create(catInfo);
            cs.create(catAcc);

            Produit p1 = new Produit("ES12", 120f, sdf.parse("01/01/2013"), catInfo);
            Produit p2 = new Produit("ZR85", 100f, sdf.parse("15/01/2013"), catInfo);
            Produit p3 = new Produit("EE85", 200f, sdf.parse("20/02/2013"), catInfo);
            Produit p4 = new Produit("CL10", 80f, sdf.parse("10/03/2013"), catAcc);

            ps.create(p1);
            ps.create(p2);
            ps.create(p3);
            ps.create(p4);

            Commande c1 = new Commande(sdf.parse("14/03/2013"));
            Commande c2 = new Commande(sdf.parse("25/04/2013"));
            cmds.create(c1);
            cmds.create(c2);

            LigneCommandeProduit lc1 = new LigneCommandeProduit(c1, p1, 7);
            LigneCommandeProduit lc2 = new LigneCommandeProduit(c1, p2, 14);
            LigneCommandeProduit lc3 = new LigneCommandeProduit(c1, p3, 5);
            LigneCommandeProduit lc4 = new LigneCommandeProduit(c2, p4, 2);

            lcs.create(lc1);
            lcs.create(lc2);
            lcs.create(lc3);
            lcs.create(lc4);

            System.out.println("\n--- 1. Produits de la categorie Informatique ---");
            List<Produit> prodsCat = ps.findByCategorie(catInfo);
            for (Produit p : prodsCat) {
                System.out.println("- " + p.getReference() + " : " + p.getPrix() + " DH");
            }

            System.out.println("\n--- 2. Produits commandes entre 01/03/2013 et 30/03/2013 ---");
            List<Produit> prodsDates = ps.findProduitsCommandesEntreDates(sdf.parse("01/03/2013"), sdf.parse("30/03/2013"));
            for (Produit p : prodsDates) {
                System.out.println("- " + p.getReference() + " : " + p.getPrix() + " DH");
            }

            System.out.println("\n--- 3. Produits dans la commande " + c1.getId() + " (Format attendu) ---");
            ps.afficherProduitsDansCommande(c1.getId());

            System.out.println("\n--- 4. Produits avec prix > 100 DH (Requete nommee) ---");
            List<Produit> chers = ps.findPrixSuperieur100();
            for (Produit p : chers) {
                System.out.println("- " + p.getReference() + " : " + p.getPrix() + " DH");
            }

            System.out.println("\n==================================================");
            System.out.println("       PARTIE 2 : BUREAU D'ETUDES");
            System.out.println("==================================================");

            EmployeService es = new EmployeService();
            ProjetService projs = new ProjetService();
            TacheService ts = new TacheService();
            EmployeTacheService ets = new EmployeTacheService();

            Employe e1 = new Employe("Alami", "Ahmed", "0612345678");
            Employe e2 = new Employe("Benani", "Fatima", "0698765432");
            es.create(e1);
            es.create(e2);

            Projet proj1 = new Projet("Gestion de stock", sdf.parse("14/01/2013"), sdf.parse("30/06/2013"), e1);
            Projet proj2 = new Projet("Portail RH", sdf.parse("01/03/2013"), sdf.parse("01/09/2013"), e2);
            projs.create(proj1);
            projs.create(proj2);

            Tache t1 = new Tache("Analyse", sdf.parse("01/02/2013"), sdf.parse("25/02/2013"), 1200.0, proj1);
            Tache t2 = new Tache("Conception", sdf.parse("01/03/2013"), sdf.parse("20/03/2013"), 800.0, proj1);
            Tache t3 = new Tache("Développement", sdf.parse("01/04/2013"), sdf.parse("30/04/2013"), 2500.0, proj1);
            ts.create(t1);
            ts.create(t2);
            ts.create(t3);

            EmployeTache et1 = new EmployeTache(e1, t1, sdf.parse("10/02/2013"), sdf.parse("20/02/2013"));
            EmployeTache et2 = new EmployeTache(e2, t2, sdf.parse("10/03/2013"), sdf.parse("15/03/2013"));
            EmployeTache et3 = new EmployeTache(e1, t3, sdf.parse("10/04/2013"), sdf.parse("25/04/2013"));
            ets.create(et1);
            ets.create(et2);
            ets.create(et3);

            System.out.println("\n--- 1. Projets geres par " + e1.getNom() + " ---");
            es.afficherProjetsGeres(e1);

            System.out.println("\n--- 2. Taches realisees par " + e1.getNom() + " ---");
            es.afficherTachesRealisees(e1);

            System.out.println("\n--- 3. Taches planifiees pour " + proj1.getNom() + " ---");
            projs.afficherTachesPlanifiees(proj1);

            System.out.println("\n--- 4. Taches realisees pour " + proj1.getNom() + " (Format attendu) ---");
            projs.afficherTachesRealisees(proj1);

            System.out.println("\n--- 5. Taches avec prix > 1000 DH (Requete nommee) ---");
            List<Tache> cheres = ts.findPrixSuperieur1000();
            for (Tache t : cheres) {
                System.out.println("- " + t.getNom() + " : " + t.getPrix() + " DH");
            }

            System.out.println("\n--- 6. Taches realisees entre 01/02/2013 et 31/03/2013 ---");
            List<Tache> entreDates = ts.findTachesRealiseesEntreDates(sdf.parse("01/02/2013"), sdf.parse("31/03/2013"));
            for (Tache t : entreDates) {
                System.out.println("- " + t.getNom());
            }

            System.out.println("\n==================================================");
            System.out.println("       PARTIE 3 : RELATIONS MATRIMONIALES");
            System.out.println("==================================================");

            HommeService hs = new HommeService();
            FemmeService fs = new FemmeService();
            MariageService ms = new MariageService();

            Homme h1 = new Homme("SAFI", "SAID", "0611111111", "Rabat", sdf.parse("15/05/1965"));
            Homme h2 = new Homme("ALAMI", "Hassan", "0622222222", "Casablanca", sdf.parse("20/08/1970"));
            Homme h3 = new Homme("BENNANI", "Omar", "0633333333", "Fès", sdf.parse("10/01/1975"));
            Homme h4 = new Homme("TAZI", "Khalid", "0644444444", "Marrakech", sdf.parse("05/11/1980"));
            Homme h5 = new Homme("IDRISSI", "Youssef", "0655555555", "Tanger", sdf.parse("12/04/1985"));

            hs.create(h1);
            hs.create(h2);
            hs.create(h3);
            hs.create(h4);
            hs.create(h5);

            Femme f1 = new Femme("RAMI", "SALIMA", "0660000001", "Rabat", sdf.parse("02/03/1968"));
            Femme f2 = new Femme("ALI", "AMAL", "0660000002", "Rabat", sdf.parse("14/07/1972"));
            Femme f3 = new Femme("ALAOUI", "WAFA", "0660000003", "Rabat", sdf.parse("22/11/1976"));
            Femme f4 = new Femme("ALAMI", "KARIMA", "0660000004", "Rabat", sdf.parse("18/09/1962"));
            Femme f5 = new Femme("BENJELLOUN", "Samira", "0660000005", "Casablanca", sdf.parse("01/01/1980"));
            Femme f6 = new Femme("CHRAIBI", "Mouna", "0660000006", "Casablanca", sdf.parse("10/10/1982"));
            Femme f7 = new Femme("BERRADA", "Khadija", "0660000007", "Fès", sdf.parse("05/05/1985"));
            Femme f8 = new Femme("EL AMRANI", "Leila", "0660000008", "Marrakech", sdf.parse("12/12/1988"));
            Femme f9 = new Femme("KABBAJ", "Nadia", "0660000009", "Tanger", sdf.parse("08/08/1990"));
            Femme f10 = new Femme("BOUZID", "Imane", "0660000010", "Agadir", sdf.parse("30/06/1992"));

            fs.create(f1);
            fs.create(f2);
            fs.create(f3);
            fs.create(f4);
            fs.create(f5);
            fs.create(f6);
            fs.create(f7);
            fs.create(f8);
            fs.create(f9);
            fs.create(f10);

            Mariage m1 = new Mariage(h1, f1, sdf.parse("03/09/1990"), null, 4);
            Mariage m2 = new Mariage(h1, f2, sdf.parse("03/09/1995"), null, 2);
            Mariage m3 = new Mariage(h1, f3, sdf.parse("04/11/2000"), null, 3);
            Mariage m4 = new Mariage(h1, f4, sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0);

            Mariage m5 = new Mariage(h2, f4, sdf.parse("15/06/1992"), null, 2);
            Mariage m6 = new Mariage(h2, f5, sdf.parse("01/01/1995"), sdf.parse("01/01/1998"), 1);
            Mariage m7 = new Mariage(h3, f5, sdf.parse("05/05/1999"), null, 2);

            ms.create(m1);
            ms.create(m2);
            ms.create(m3);
            ms.create(m4);
            ms.create(m5);
            ms.create(m6);
            ms.create(m7);

            System.out.println("\n--- 1. Liste des femmes ---");
            List<Femme> toutesLesFemmes = fs.findAll();
            for (Femme f : toutesLesFemmes) {
                System.out.println("- " + f.getNom() + " " + f.getPrenom() + " (Nee le: " + sdf.format(f.getDateNaissance()) + ")");
            }

            System.out.println("\n--- 2. Femme la plus agee ---");
            Femme ainee = fs.findFemmePlusAgee();
            if (ainee != null) {
                System.out.println(ainee.getNom() + " " + ainee.getPrenom() + " (Nee le: " + sdf.format(ainee.getDateNaissance()) + ")");
            }

            System.out.println("\n--- 3. Epouses de SAFI SAID entre 01/01/1990 et 31/12/2005 ---");
            List<Femme> epouses = hs.findEpousesEntreDates(h1, sdf.parse("01/01/1990"), sdf.parse("31/12/2005"));
            for (Femme f : epouses) {
                System.out.println("- " + f.getNom() + " " + f.getPrenom());
            }

            System.out.println("\n--- 4. Nombre d'enfants de SALIMA RAMI entre 01/01/1985 et 31/12/2010 (Requete native) ---");
            int nbrEnfants = fs.countEnfantsEntreDates(f1, sdf.parse("01/01/1985"), sdf.parse("31/12/2010"));
            System.out.println("Nombre d'enfants : " + nbrEnfants);

            System.out.println("\n--- 5. Femmes mariees au moins deux fois (Requete nommee) ---");
            List<Femme> marieesMultiples = fs.findFemmesMarieesAuMoinsDeuxFois();
            for (Femme f : marieesMultiples) {
                System.out.println("- " + f.getNom() + " " + f.getPrenom());
            }

            System.out.println("\n--- 6. Nombre d'hommes maries a 4 femmes entre 01/01/1980 et 31/12/2005 (Criteria API) ---");
            int nbrHommes4 = ms.countHommesMariesQuatreFemmes(sdf.parse("01/01/1980"), sdf.parse("31/12/2005"));
            System.out.println("Nombre d'hommes : " + nbrHommes4);

            System.out.println("\n--- 7. Mariages de SAFI SAID (Format attendu) ---");
            hs.afficherMariages(h1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            try {
                com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            } catch (Exception ignored) {
            }
        }
    }
}
