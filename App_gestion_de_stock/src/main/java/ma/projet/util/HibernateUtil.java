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
            try (InputStream input = HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {
                if (input == null) throw new RuntimeException("application.properties introuvable");
                props.load(input);
            }

            Configuration cfg = new Configuration().setProperties(props);
            cfg.addAnnotatedClass(ma.projet.classes.Categorie.class);
            cfg.addAnnotatedClass(ma.projet.classes.Produit.class);
            cfg.addAnnotatedClass(ma.projet.classes.Commande.class);
            cfg.addAnnotatedClass(ma.projet.classes.LigneCommandeProduit.class);

            ServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(cfg.getProperties()).build();
            return cfg.buildSessionFactory(registry);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() { return sessionFactory; }
    public static void shutdown() { sessionFactory.close(); }
}
