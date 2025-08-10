package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id SERIAL PRIMARY KEY, "
                + "name VARCHAR(100), "
                + "lastName VARCHAR(100), "
                + "age SMALLINT"
                + ")";  // Закрывающая скобка добавлена

        try (Connection connection = Util.getConnection(); // Соединение с базой данных
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate(); // Выполняем создание таблицы
            System.out.println("Таблица 'users' успешно создана.");
        } catch (SQLException e) { // Обрабатывает SQL-исключения
            e.printStackTrace(); // Выводит стек вызовов для диагностики
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем — " + name +" "+ lastName +" добавлен в базу данных.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.getConnection(); //соединение с базой данных
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();//выполняет обновление базы данных (таблицы)
        } catch (SQLException e) { //обрабатывает SQL-исключения
            e.printStackTrace(); //выводит стек вызовов для диагностики
        }
    }


    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            System.out.println("Пользователь с id=" + id + " удален");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                Byte age = resultSet.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            System.out.println("Таблица пользователей очищена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}