package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties props = new Properties();
            try (InputStream in = HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {
                if (in == null) throw new RuntimeException("application.properties introuvable");
                props.load(in);
            }

            Configuration cfg = new Configuration().setProperties(props);
            cfg.addAnnotatedClass(ma.projet.beans.Homme.class);
            cfg.addAnnotatedClass(ma.projet.beans.Femme.class);
            cfg.addAnnotatedClass(ma.projet.beans.Mariage.class);
            cfg.addAnnotatedClass(ma.projet.beans.Personne.class);

            ServiceRegistry reg = new StandardServiceRegistryBuilder()
                    .applySettings(cfg.getProperties()).build();
            return cfg.buildSessionFactory(reg);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
