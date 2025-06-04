package penjualandetil.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import penjualandetil.entity.User;
import penjualandetil.service.UserService;

public class UserServiceImpl implements UserService {
    private final SessionFactory sessionFactory;

    public UserServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace(); // Handle logging/exception
            return null;
        }
    }
}