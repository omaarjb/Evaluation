package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategorieService implements IDao<Categorie> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Categorie add(Categorie o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    @Transactional
    public Categorie update(Categorie o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Categorie c = getById(id);
        if (c != null) {
            sessionFactory.getCurrentSession().delete(c);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Categorie getById(Long id) {
        return sessionFactory.getCurrentSession().get(Categorie.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categorie> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Categorie", Categorie.class)
                .list();
    }
}
