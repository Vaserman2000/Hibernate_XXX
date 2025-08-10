package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;

public class UserHibernateDaoImpl implements UserDao {
    public UserHibernateDaoImpl() {
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(100), lastName VARCHAR(100), age SMALLINT)";
        try (Session session = Util.getSessionFactory().openSession()) {
            //- Открывается сессия Hibernate с использованием Util, который предоставляет SessionFactory.
            //- Используется конструкция try-with-resources для автоматического закрытия сессии.
            Transaction transaction = session.beginTransaction();
            //- Начинается новая транзакция для обеспечения атомарности операций.
            session.createNativeQuery(sql).executeUpdate();
            //- Выполняется запрос на создание таблицы. Метод createNativeQuery позволяет использовать SQL-запросы напрямую.
            transaction.commit();
            //- Завершение транзакции, что означает, что все изменения будут применены. Если произошла ошибка, транзакция может быть отменена (rollback).
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users"; //- Создается строка SQL для удаления таблицы users, если она существует.
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            //- Логика здесь аналогична предыдущему методу:
            // открывается сессия, начинается транзакция,
            // выполняется SQL-запрос на удаление таблицы и транзакция завершается.
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        if (name == null || name.isEmpty() || name.length() > 20) {
            System.out.println("Имя должно быть не пустым и меньше 20 символов. Пожалуйста, исправьте ввод.");
            return;
        }

        if (lastName == null || lastName.isEmpty() || lastName.length() > 20) {
            System.out.println("Фамилия должна быть не пустой и меньше 20 символов. Пожалуйста, исправьте ввод.");
            return;
        }

        if (age < 1 || age > 120) {
            System.out.println("Возраст должен быть в диапазоне от 1 до 120. Пожалуйста, исправьте ввод.");
            return;
        }

        User user = new User(name, lastName, age); // Создаем новый объект User
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user); // Сохраняем нового пользователя в базе данных
            transaction.commit();// Зафиксировать транзакцию
            System.out.println("User (" + name +" " + lastName + ") добавлен в базу данных.");
        } catch (ConstraintViolationException e) {
            System.out.println("Пользователь с такими данными уже существует. Пожалуйста, введите данные заново.");
        } catch (Exception e) {
            e.printStackTrace(); // Логирование других возможных ошибок
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id); // Находим пользователя по id
            if (user != null) {
                session.delete(user); // Если существует, удаляем
                System.out.println("Пользователь с ID " + id + " удален."); // Сообщение об удалении
            } else {
                System.out.println("Пользователь с ID " + id + " не найден."); // Сообщение о том, что пользователь не найден
            }
            transaction.commit(); // Подтверждаем транзакцию
        } catch (Exception e) {
            e.printStackTrace(); // Обработка ошибок
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users; //- Объявление списка users, который будет использоваться для хранения всех пользователей.
        try (Session session = Util.getSessionFactory().openSession()) { //- Открывается сессия.
            Transaction transaction = session.beginTransaction(); //- Начинается транзакция.
            users = session.createQuery("FROM User", User.class).list(); // Получаем всех пользователей из базы
            // - SQL-запрос, написанный в стиле HQL (Hibernate Query Language), который выбирает всех пользователей.
            // - Список пользователей будет заполнен результатами запроса.
            transaction.commit(); //- Завершение транзакции.
        }
        return users; //- Возвращает список пользователей.
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users"; //- Создается строка SQL для очистки таблицы users, удаляющая все записи.
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Очистил нахуй всю таблицу");
            //- Логика аналогична предыдущим методам: открытие сессии и транзакции, выполнение SQL-запроса, завершение транзакции.
        }
    }
}
