package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MariageService implements IDao<Mariage> {

    @Override
    public Mariage add(Mariage o) {
        Session s = null;
        Transaction tx = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            tx = s.beginTransaction();
            s.persist(o);
            tx.commit();
            return o;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public Mariage update(Mariage o) {
        Session s = null;
        Transaction tx = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            tx = s.beginTransaction();
            s.merge(o);
            tx.commit();
            return o;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session s = null;
        Transaction tx = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            tx = s.beginTransaction();
            Mariage m = s.get(Mariage.class, id);
            if (m != null) s.remove(m);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public Mariage getById(Long id) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.get(Mariage.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
    }

    @Override
    public List<Mariage> getAll() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            return s.createQuery("FROM Mariage", Mariage.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (s != null) s.close();
        }
    }

    public List<Object[]> mariagesHommeDetails(Long hommeId){
        Session s=null;
        try{
            s=HibernateUtil.getSessionFactory().openSession();
            return s.createQuery(
                            "SELECT f.nom, f.prenom, m.dateDebut, m.dateFin, m.nbEnfants " +
                                    "FROM Mariage m JOIN m.femme f " +
                                    "WHERE m.homme.id = :hid ORDER BY m.dateDebut",
                            Object[].class)
                    .setParameter("hid", hommeId)
                    .getResultList();
        }catch(Exception e){ e.printStackTrace(); return List.of(); }
        finally{ if(s!=null) s.close(); }
    }

}
