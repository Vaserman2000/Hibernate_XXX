package gpt5.utilGPT;

import gpt5.modelGPT.UserGPT;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            Properties settings = new Properties();

            // Параметры по умолчанию; при желании можно передавать через System properties / env vars
            settings.put(Environment.DRIVER, "org.postgresql.Driver");
            settings.put(Environment.URL, System.getProperty("db.url", "jdbc:postgresql://localhost:5432/sergejvasilcikov"));
            settings.put(Environment.USER, System.getProperty("db.user", "sergejvasilcikov"));
            settings.put(Environment.PASS, System.getProperty("db.pass", "sergejvasilcikov"));
            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

            // Для разработки удобно update; в продакшн заменять на validate или отключать
            settings.put(Environment.HBM2DDL_AUTO, System.getProperty("hibernate.hbm2ddl.auto", "update"));
            settings.put(Environment.SHOW_SQL, System.getProperty("hibernate.show_sql", "true"));
            settings.put(Environment.FORMAT_SQL, "true");

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(UserGPT.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null) {
            SESSION_FACTORY.close();
        }
    }
}
