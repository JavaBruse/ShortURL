package org.MIFI.service;

import org.MIFI.GRUD.UserDAO;
import org.MIFI.entity.User;

import java.util.UUID;

public class UserService {
    UserDAO userDAO = new UserDAO();

    public User addUser(String name) {
        User user = new User();
        user.setName(name);
        user.setUUID(UUID.randomUUID().toString());
        return userDAO.save(user);
    }

    public String getByUUID(String UUID) {
        User user = userDAO.findByUUID(UUID);
        if (user.getUUID() != null) {
            return user.getName();
        }
        return null;
    }

//    public boolean checkUserUUID(String UUID)  {
//        User user = userDAO.findByUUID(UUID);
//        if (user.getUUID() != null) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }
}
