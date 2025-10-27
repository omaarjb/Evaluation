package ma.projet.service;

import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetService implements IDao<Projet> {

    @Override
    public Projet add(Projet o) {
        Session s = null;
        Transaction tx = null;
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
    public Projet update(Projet o) {
        Session s = null;
        Transaction tx = null;
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
        Session s = null;
        Transaction tx = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            tx = s.beginTransaction();
            Projet p = s.get(Projet.class, id);
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
    public Projet getById(Long id) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.get(Projet.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public List<Projet> getAll() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery("FROM Projet", Projet.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }

    public List<Tache> tachesPlanifieesPourProjet(Long projetId) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery(
                    "FROM Tache t WHERE t.projet.id = :pid ORDER BY t.dateDebutPlanifiee",
                    Tache.class).setParameter("pid", projetId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }

    public List<Object[]> tachesRealiseesAvecDates(Long projetId) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery(
                    "SELECT t.id, t.nom, et.dateDebutReelle, et.dateFinReelle " +
                            "FROM EmployeTache et JOIN et.tache t " +
                            "WHERE t.projet.id = :pid ORDER BY t.id",
                    Object[].class).setParameter("pid", projetId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }
}
