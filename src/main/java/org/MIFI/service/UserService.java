package org.MIFI.service;

import org.MIFI.GRUD.UserDAO;
import org.MIFI.entity.User;

import java.util.UUID;

public class UserService {
    UserDAO userDAO = new UserDAO();

    public User addUser(String name) {
        User user = new User();
        user.setUUID(UUID.randomUUID().toString());
        return userDAO.save(user);
    }

    public User getByUUID(String UUID) {
        return userDAO.findByUUID(UUID);
    }

    public boolean checkUserUUID(String UUID) {
        if (userDAO.findByUUID(UUID) == null) {
            return false;
        } else {
            return true;
        }

    }
}
