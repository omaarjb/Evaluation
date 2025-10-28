package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeTacheService implements IDao<EmployeTache> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EmployeTache add(EmployeTache o) {
        sessionFactory.getCurrentSession().save(o);
        return o;
    }

    @Override
    public EmployeTache update(EmployeTache o) {
        sessionFactory.getCurrentSession().update(o);
        return o;
    }

    @Override
    public void delete(Long id) {
        var session = sessionFactory.getCurrentSession();
        EmployeTache et = session.get(EmployeTache.class, id);
        if (et != null) session.delete(et);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeTache getById(Long id) {
        return sessionFactory.getCurrentSession().get(EmployeTache.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeTache> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from EmployeTache", EmployeTache.class)
                .list();
    }
}
