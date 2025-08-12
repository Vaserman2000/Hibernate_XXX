package gpt5.servisGPT;

import gpt5.daoGPT.UserDaoGPT;
import gpt5.modelGPT.UserGPT;

import java.util.List;
import java.util.Objects;

public class UserServiceImpl implements UserService {

    private final UserDaoGPT userDaoGPT;

    public UserServiceImpl(UserDaoGPT userDao) {
        this.userDaoGPT = Objects.requireNonNull(userDao, "UserDao cannot be null");
    }

    @Override
    public void createUsersTable() {
        userDaoGPT.createUsersTable();
        System.out.println("Таблица пользователей создана (если отсутствовала).");
    }

    @Override
    public void dropUsersTable() {
        userDaoGPT.dropUsersTable();
        System.out.println("Таблица пользователей удалена (если существовала).");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            validateUserData(name, lastName, age);
            userDaoGPT.saveUser(name.trim(), lastName.trim(), age);
            System.out.printf("Пользователь с именем %s %s (возраст %d) добавлен в базу данных.%n", name, lastName, age);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        if (id <= 0) throw new IllegalArgumentException("ID должен быть положительным");

        // Перед удалением проверяем, существует ли пользователь
        boolean exists = getAllUsers().stream().anyMatch(u -> u.getId() == id);

        if (exists) {
            userDaoGPT.removeUserById(id);
            System.out.println("Пользователь с ID " + id + " удалён.");
        } else {
            System.out.println("Пользователя с ID " + id + " не существует.");
        }
    }


    @Override
    public List<UserGPT> getAllUsers() {
        List<UserGPT> users = userDaoGPT.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        userDaoGPT.cleanUsersTable();
        System.out.println("Таблица пользователей очищена.");
    }

    private void validateUserData(String name, String lastName, byte age) {
        if (name == null || !name.matches("^[А-Яа-яA-Za-z]+$") || name.length() > 50) {
            throw new IllegalArgumentException("Имя должно состоять только из букв (русских или латинских) и быть не длиннее 50 символов");
        }
        if (lastName == null || !lastName.matches("^[А-Яа-яA-Za-z]+$") || lastName.length() > 50) {
            throw new IllegalArgumentException("Фамилия должна состоять только из букв (русских или латинских) и быть не длиннее 50 символов");
        }
        if (age < 18 || age > 100) {
            throw new IllegalArgumentException("Возраст должен быть от 18 до 100 лет");
        }
    }

}
