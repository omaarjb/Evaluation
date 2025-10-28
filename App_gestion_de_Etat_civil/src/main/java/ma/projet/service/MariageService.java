package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MariageService implements IDao<Mariage> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Mariage add(Mariage o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    public Mariage update(Mariage o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Mariage m = session.get(Mariage.class, id);
        if (m != null) session.delete(m);
    }

    @Override
    @Transactional(readOnly = true)
    public Mariage getById(Long id) {
        return sessionFactory.getCurrentSession().get(Mariage.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mariage> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Mariage", Mariage.class)
                .list();
    }

    @Transactional(readOnly = true)
    public List<Object[]> mariagesHommeDetails(Long hommeId) {
        String hql = """
                SELECT f.nom, f.prenom, m.dateDebut, m.dateFin, m.nbEnfants
                FROM Mariage m
                JOIN m.femme f
                WHERE m.homme.id = :hid
                ORDER BY m.dateDebut
                """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Object[].class)
                .setParameter("hid", hommeId)
                .getResultList();
    }
}
