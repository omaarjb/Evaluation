package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CommandeService implements IDao<Commande> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Commande add(Commande o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    @Transactional
    public Commande update(Commande o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Commande c = getById(id);
        if (c != null) {
            sessionFactory.getCurrentSession().delete(c);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Commande getById(Long id) {
        return sessionFactory.getCurrentSession().get(Commande.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commande> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Commande", Commande.class)
                .list();
    }
}
