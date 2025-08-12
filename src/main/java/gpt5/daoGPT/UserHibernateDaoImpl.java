package gpt5.daoGPT;

import gpt5.modelGPT.UserGPT;
import gpt5.utilGPT.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Function;

public class UserHibernateDaoImpl implements UserDaoGPT {

    private <R> R execute(Function<Session, R> action, boolean transactional) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (transactional) tx = session.beginTransaction();
            R result = action.apply(session);
            if (transactional && tx != null) tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx != null && tx.getStatus().canRollback()) tx.rollback();
            throw e;
        }
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "lastName VARCHAR(50) NOT NULL, " +
                "age SMALLINT NOT NULL)";
        execute(s -> s.createNativeQuery(sql).executeUpdate(), true);
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        execute(s -> s.createNativeQuery(sql).executeUpdate(), true);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        execute(s -> {
            s.persist(new UserGPT(name, lastName, age));
            return null;
        }, true);
    }

    @Override
    public void removeUserById(long id) {
        execute(s -> {
            UserGPT user = s.get(UserGPT.class, id);
            if (user != null) {
                s.remove(user);
            }
            return null;
        }, true);
    }


    @Override
    public List<UserGPT> getAllUsers() {
        return execute(s -> s.createQuery("FROM UserGPT", UserGPT.class).list(), false);
    }

    @Override
    public void cleanUsersTable() {
        execute(s -> s.createNativeQuery("TRUNCATE TABLE users").executeUpdate(), true);
    }
}
