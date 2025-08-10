package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Util {
    // реализуйте настройку соединения с БД
    private static final String URL = "jdbc:postgresql://localhost:5432/sergejvasilcikov";
    private static final String USER = "sergejvasilcikov";
    private static final String PASSWORD = "sergejvasilcikov";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    // Hibernate
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        try {
            // Создание конфигурации Hibernate
            Configuration configuration = new Configuration();

            // Настройка свойств соединения
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/sergejvasilcikov");
            configuration.setProperty("hibernate.connection.username", "sergejvasilcikov");
            configuration.setProperty("hibernate.connection.password", "sergejvasilcikov");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update"); // или "create" для создания таблиц
            configuration.setProperty("hibernate.show_sql", "true"); // вывод SQL-запросов в консоль

            // Добавьте класс User (сущность) в конфигурацию
            configuration.addAnnotatedClass(jm.task.core.jdbc.model.User.class);

            // Построение SessionFactory
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Закрытие кэша сессий
        getSessionFactory().close();
    }
}
