package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FemmeService implements IDao<Femme> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Femme add(Femme o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    public Femme update(Femme o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Femme f = session.get(Femme.class, id);
        if (f != null) session.delete(f);
    }

    @Override
    @Transactional(readOnly = true)
    public Femme getById(Long id) {
        return sessionFactory.getCurrentSession().get(Femme.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Femme> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Femme", Femme.class)
                .list();
    }

    // === Custom Queries ===

    @Transactional(readOnly = true)
    public int nbEnfantsEntreDates(Long femmeId, LocalDate d1, LocalDate d2) {
        String sql = """
                SELECT COALESCE(SUM(nbrEnfants), 0)
                FROM mariages
                WHERE femme_id = :fid AND dateDebut BETWEEN :d1 AND :d2
                """;
        Integer result = sessionFactory.getCurrentSession()
                .createNativeQuery(sql, Integer.class)
                .setParameter("fid", femmeId)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .getSingleResult();
        return result != null ? result : 0;
    }

    @Transactional(readOnly = true)
    public List<Femme> femmesMarieesDeuxFoisOuPlus() {
        String sql = """
                SELECT f.*
                FROM femmes f
                JOIN mariages m ON m.femme_id = f.id
                GROUP BY f.id
                HAVING COUNT(m.id) >= 2
                """;
        return sessionFactory.getCurrentSession()
                .createNativeQuery(sql, Femme.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public long nbHommesMarieAQuatreFemmesEntre(LocalDate d1, LocalDate d2) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Mariage> root = cq.from(Mariage.class);

        cq.select(cb.countDistinct(root.get("homme").get("id")))
                .where(cb.between(root.get("dateDebut"), d1, d2))
                .groupBy(root.get("homme").get("id"))
                .having(cb.equal(cb.count(root.get("id")), 4L));

        List<Long> ids = session.createQuery(cq).getResultList();
        return ids.size();
    }
}
