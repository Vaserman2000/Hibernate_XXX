package gpt5.servisGPT;

import gpt5.modelGPT.UserGPT;

import java.util.List;

public interface UserService {
    void createUsersTable();
    void dropUsersTable();
    void saveUser(String name, String lastName, byte age);
    void removeUserById(long id);
    List<UserGPT> getAllUsers();
    void cleanUsersTable();
}
