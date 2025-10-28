package ma.projet.service;

import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjetService implements IDao<Projet> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Projet add(Projet o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    public Projet update(Projet o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    public void delete(Long id) {
        var session = sessionFactory.getCurrentSession();
        Projet p = session.get(Projet.class, id);
        if (p != null) session.delete(p);
    }

    @Override
    @Transactional(readOnly = true)
    public Projet getById(Long id) {
        return sessionFactory.getCurrentSession().get(Projet.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Projet> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Projet", Projet.class)
                .list();
    }

    /**
     * 🔹 Returns all planned tasks for a given project.
     */
    @Transactional(readOnly = true)
    public List<Tache> tachesPlanifieesPourProjet(Long projetId) {
        String hql = """
            from Tache t 
            where t.projet.id = :pid 
            order by t.dateDebutPlanifiee
            """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Tache.class)
                .setParameter("pid", projetId)
                .getResultList();
    }

    /**
     * 🔹 Returns all completed tasks with their actual start and end dates.
     */
    @Transactional(readOnly = true)
    public List<Object[]> tachesRealiseesAvecDates(Long projetId) {
        String hql = """
            select t.id, t.nom, et.dateDebutReelle, et.dateFinReelle
            from EmployeTache et 
            join et.tache t 
            where t.projet.id = :pid 
            order by t.id
            """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Object[].class)
                .setParameter("pid", projetId)
                .getResultList();
    }
}
