package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ProduitService implements IDao<Produit> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Produit add(Produit o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    @Transactional
    public Produit update(Produit o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Produit p = getById(id);
        if (p != null) {
            sessionFactory.getCurrentSession().delete(p);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Produit getById(Long id) {
        return sessionFactory.getCurrentSession().get(Produit.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Produit", Produit.class)
                .list();
    }

    // === Custom Queries ===

    @Transactional(readOnly = true)
    public List<Object[]> produitsParCommande(Long commandeId) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT p.reference, p.prix, l.quantite " +
                                "FROM LigneCommandeProduit l " +
                                "JOIN l.produit p " +
                                "WHERE l.commande.id = :cid " +
                                "ORDER BY p.reference",
                        Object[].class)
                .setParameter("cid", commandeId)
                .list();
    }

    @Transactional(readOnly = true)
    public List<Object[]> produitsCommandesEntre(LocalDate d1, LocalDate d2) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT p.reference, p.prix, SUM(l.quantite) " +
                                "FROM LigneCommandeProduit l " +
                                "JOIN l.produit p " +
                                "JOIN l.commande c " +
                                "WHERE c.date BETWEEN :d1 AND :d2 " +
                                "GROUP BY p.id, p.reference, p.prix " +
                                "ORDER BY p.reference",
                        Object[].class)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .list();
    }

    @Transactional(readOnly = true)
    public List<Produit> findByCategorie(Categorie categorie) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Produit p WHERE p.categorie = :cat", Produit.class)
                .setParameter("cat", categorie)
                .list();
    }

    @Transactional(readOnly = true)
    public List<Produit> prixSup100() {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("Produit.prixSup100", Produit.class)
                .getResultList();
    }
}
