package org.MIFI.service;

import org.MIFI.GRUD.UserDAO;
import org.MIFI.entity.User;

import java.util.UUID;

public class UserService {
    UserDAO userDAO = new UserDAO();

    public void addUser(String name) {
        User user = new User();
        user.setUUID(UUID.randomUUID().toString());
        userDAO.save(user);
    }

    public User getByUUID(String UUID){
       return userDAO.findByUUID(UUID);
    }
}
