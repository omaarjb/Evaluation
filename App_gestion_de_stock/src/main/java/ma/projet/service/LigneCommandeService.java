package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class LigneCommandeService implements IDao<LigneCommandeProduit> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public LigneCommandeProduit add(LigneCommandeProduit o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    @Transactional
    public LigneCommandeProduit update(LigneCommandeProduit o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LigneCommandeProduit l = getById(id);
        if (l != null) {
            sessionFactory.getCurrentSession().delete(l);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LigneCommandeProduit getById(Long id) {
        return sessionFactory.getCurrentSession().get(LigneCommandeProduit.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneCommandeProduit> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from LigneCommandeProduit", LigneCommandeProduit.class)
                .list();
    }
}
