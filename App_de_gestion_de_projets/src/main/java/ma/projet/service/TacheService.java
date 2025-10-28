package ma.projet.service;

import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TacheService implements IDao<Tache> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Tache add(Tache o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    public Tache update(Tache o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    public void delete(Long id) {
        var session = sessionFactory.getCurrentSession();
        Tache t = session.get(Tache.class, id);
        if (t != null) session.delete(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Tache getById(Long id) {
        return sessionFactory.getCurrentSession().get(Tache.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tache> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Tache", Tache.class)
                .list();
    }


    @Transactional(readOnly = true)
    public List<Tache> prixSup1000() {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("Tache.prixSup1000", Tache.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Object[]> tachesRealiseesEntre(LocalDate d1, LocalDate d2) {
        String hql = """
            select t.id, t.nom, et.dateDebutReelle, et.dateFinReelle
            from EmployeTache et 
            join et.tache t 
            where et.dateDebutReelle >= :d1 
              and et.dateFinReelle <= :d2 
            order by et.dateDebutReelle
            """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Object[].class)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .getResultList();
    }
}
