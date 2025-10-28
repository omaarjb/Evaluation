package ma.projet.service;

import ma.projet.classes.Employe;
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
public class EmployeService implements IDao<Employe> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Employe add(Employe o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    public Employe update(Employe o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    public void delete(Long id) {
        var session = sessionFactory.getCurrentSession();
        Employe e = session.get(Employe.class, id);
        if (e != null) session.delete(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Employe getById(Long id) {
        return sessionFactory.getCurrentSession().get(Employe.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Employe", Employe.class)
                .list();
    }


    @Transactional(readOnly = true)
    public List<Tache> tachesRealiseesParEmploye(Long employeId) {
        String hql = """
            SELECT et.tache 
            FROM EmployeTache et 
            WHERE et.employe.id = :eid 
            ORDER BY et.tache.id
            """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Tache.class)
                .setParameter("eid", employeId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Projet> projetsGeresParEmploye(Long employeId) {
        String hql = """
            FROM Projet p 
            WHERE p.chefDeProjet.id = :eid 
            ORDER BY p.id
            """;
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Projet.class)
                .setParameter("eid", employeId)
                .getResultList();
    }
}
