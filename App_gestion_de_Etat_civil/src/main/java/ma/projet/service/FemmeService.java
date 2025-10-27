package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import ma.projet.beans.Mariage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FemmeService implements IDao<Femme> {
    @Override
    public Femme add(Femme o) {
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
    public Femme update(Femme o) {
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
            Femme f = s.get(Femme.class, id);
            if (f != null) s.remove(f);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public Femme getById(Long id) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.get(Femme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public List<Femme> getAll() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery("FROM Femme", Femme.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }


    public int nbEnfantsEntreDates(Long femmeId, LocalDate d1, LocalDate d2) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            var q = s.createNativeQuery(
                    "SELECT COALESCE(SUM(nbrEnfants), 0) " +
                            "FROM mariages " +
                            "WHERE femme_id = :fid AND dateDebut BETWEEN :d1 AND :d2",
                    Integer.class
            );
            q.setParameter("fid", femmeId);
            q.setParameter("d1", d1);
            q.setParameter("d2", d2);
            Integer res = q.getSingleResult();
            return res == null ? 0 : res;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (s != null) s.close();
        }
    }


    public List<Femme> femmesMarieesDeuxFoisOuPlus() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            var q = s.createNativeQuery(
                    "SELECT f.* " +
                            "FROM femmes f " +
                            "JOIN mariages m ON m.femme_id = f.id " +
                            "GROUP BY f.id " +
                            "HAVING COUNT(m.id) >= 2",
                    Femme.class
            );
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }


    public long nbHommesMarieAQuatreFemmesEntre(LocalDate d1, LocalDate d2) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Mariage> root = cq.from(Mariage.class);

            // count distinct hommes having exactly 4 mariages in range
            cq.select(cb.countDistinct(root.get("homme").get("id")))
                    .where(cb.between(root.get("dateDebut"), d1, d2))
                    .groupBy(root.get("homme").get("id"))
                    .having(cb.equal(cb.count(root.get("id")), 4L));

            List<Long> ids = s.createQuery(cq).getResultList();
            return ids.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (s != null) s.close();
        }
    }
}
