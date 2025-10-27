package ma.projet.service;

import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProduitService implements IDao<Produit> {

    @Override
    public Produit add(Produit o) {
        Session s = null; Transaction tx = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            tx = s.beginTransaction();
            s.persist(o);
            tx.commit();
            return o;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public Produit update(Produit o) {
        Session s = null; Transaction tx = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            tx = s.beginTransaction();
            s.merge(o);
            tx.commit();
            return o;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session s = null; Transaction tx = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            tx = s.beginTransaction();
            Produit p = s.get(Produit.class, id);
            if (p != null) s.remove(p);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public Produit getById(Long id) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.get(Produit.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public List<Produit> getAll() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery("FROM Produit", Produit.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }

    public List<Produit> findByCategorie(Long categorieId) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery(
                            "FROM Produit p WHERE p.categorie.id = :cid", Produit.class)
                    .setParameter("cid", categorieId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }

    public List<Object[]> produitsCommandesEntre(LocalDate d1, LocalDate d2) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery(
                            "SELECT p.reference, p.prix, SUM(l.quantite) " +
                                    "FROM LigneCommandeProduit l " +
                                    "JOIN l.produit p " +
                                    "JOIN l.commande c " +
                                    "WHERE c.date BETWEEN :d1 AND :d2 " +
                                    "GROUP BY p.id, p.reference, p.prix " +
                                    "ORDER BY p.reference", Object[].class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }

    public List<Object[]> produitsParCommande(Long commandeId) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery(
                            "SELECT p.reference, p.prix, l.quantite " +
                                    "FROM LigneCommandeProduit l " +
                                    "JOIN l.produit p " +
                                    "WHERE l.commande.id = :cid " +
                                    "ORDER BY p.reference", Object[].class)
                    .setParameter("cid", commandeId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }

    public List<Produit> prixSup100() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createNamedQuery("Produit.prixSup100", Produit.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }
}
