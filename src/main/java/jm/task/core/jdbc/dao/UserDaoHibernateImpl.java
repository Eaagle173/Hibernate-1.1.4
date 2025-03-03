package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory factory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastNAme VARCHAR(50), " +
                "age SMALLINT)";
        try (Session session1 = factory.getCurrentSession()) {
            session1.beginTransaction();
            session1.createNativeQuery(sql).executeUpdate();
            session1.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Error creating users table: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Session session1 = factory.getCurrentSession()) {
            session1.beginTransaction();
            session1.createNativeQuery(sql).executeUpdate();
            session1.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Error dropping users table: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session1 = factory.getCurrentSession()) {
            session1.beginTransaction();
            session1.save(new User(name, lastName, age));
            session1.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session1 = factory.getCurrentSession()) {
            session1.beginTransaction();
            User user = session1.get(User.class, id);
            session1.delete(user);
            session1.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        try (Session session1 = factory.getCurrentSession()) {
            session1.beginTransaction();
            Query<User> query = session1.createQuery("from User", User.class);
            users = query.getResultList();
            session1.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Error getting all users: " + e.getMessage());
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users RESTART IDENTITY CASCADE";
        try (Session session1 = factory.getCurrentSession()) {
            session1.beginTransaction();
            session1.createNativeQuery(sql).executeUpdate();
            session1.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Error cleaning users table: " + e.getMessage());
        }
    }
}
