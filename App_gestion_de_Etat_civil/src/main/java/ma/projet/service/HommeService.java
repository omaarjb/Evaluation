package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class HommeService implements IDao<Homme> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Homme add(Homme o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    public Homme update(Homme o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Homme h = session.get(Homme.class, id);
        if (h != null) session.delete(h);
    }

    @Override
    @Transactional(readOnly = true)
    public Homme getById(Long id) {
        return sessionFactory.getCurrentSession().get(Homme.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Homme> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Homme", Homme.class)
                .list();
    }

    // === Custom Query ===

    @Transactional(readOnly = true)
    public List<Femme> epousesEntreDates(Long hommeId, LocalDate d1, LocalDate d2) {
        String hql = """
                SELECT m.femme FROM Mariage m
                WHERE m.homme.id = :hid
                AND m.dateDebut BETWEEN :d1 AND :d2
                ORDER BY m.dateDebut
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Femme.class)
                .setParameter("hid", hommeId)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .getResultList();
    }
}
