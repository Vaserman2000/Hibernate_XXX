package gpt5.daoGPT;

import gpt5.modelGPT.UserGPT;

import java.util.List;

public interface UserDaoGPT {
    void createUsersTable();
    void dropUsersTable();
    void saveUser(String name, String lastName, byte age);
    void removeUserById(long id);
    List<UserGPT> getAllUsers();
    void cleanUsersTable();
}
