package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserHibernateDaoImpl;
import jm.task.core.jdbc.model.User;
import java.util.List;


public class UserServiceImpl implements UserService {

    private final UserDao userDao; //Поле, хранящее экземпляр UserDao
    public UserServiceImpl() { //Конструктор, который инициализирует объект UserDao
        this.userDao = new UserHibernateDaoImpl();
    }

    @Override
    public void createUsersTable() { //Метод для создания таблицы пользователей
        userDao.createUsersTable(); //Делегирует вызов метода DAO
    }

    @Override
    public void saveUser(String name, String lastName, byte age) { //Метод для добавления нового пользователя
        userDao.saveUser(name, lastName, age); //Делегирует метод DAO
    }

    @Override
    public void dropUsersTable() {
        userDao.dropUsersTable();
    }


    @Override
    public void removeUserById(long id) { //Метод для удаления пользователя по ID
        userDao.removeUserById(id);  //Делегирует метод DAO
    }

    @Override
    public List<User> getAllUsers() { //Метод для получения всех пользователей в виде списка
        return userDao.getAllUsers(); //Делегирует метод DAO и возвращает результат
    }

    @Override
    public void cleanUsersTable() { //Метод для очистки таблицы пользователей
        userDao.cleanUsersTable();
    }
}
