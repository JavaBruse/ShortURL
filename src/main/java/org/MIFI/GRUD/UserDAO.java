package org.MIFI.GRUD;

import lombok.SneakyThrows;
import org.MIFI.DataBaseUtils;
import org.MIFI.entity.Entity;
import org.MIFI.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO implements DAO {

    private Statement statement;

    public UserDAO(Statement statement) {
        this.statement = DataBaseUtils.getInstance().getStmt();
    }

    @Override
    public User findById(Long id) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE ID LIKE " + id + ";");
            User user = new User();
            user.setId((long) rs.getInt(1));
            user.setName(rs.getString(2));
            user.setUUID(rs.getString(3));
            return user;
        } catch (SQLException e) {
            return  null;
        }
    }

    @Override
    public boolean saveOrUpdate(Entity entity) {
        User user = (User) entity;
        User u = (User) findByName(user.getName());
        if (u != null) user.setId(u.getId());
        if (user.getId() == -1) {
            return save(user);
        } else {
            return update(user);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            int x = statement.executeUpdate("delete from users WHERE id = " + id + ";");
            return x == 1 ? true : false;
        } catch (SQLException e) {
            return false;
        }
    }

    public Entity findByName(String name) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE name LIKE '" + name + "';");
            if (rs == null || rs.getString(1).equals("id")) return null;
            User user  = new User();
            user.setId((long) rs.getInt(1));
            user.setName(rs.getString(2));
            user.setUUID(rs.getString(3));
            return user;
        } catch (SQLException e) {
            return null;
        }
    }


    @SneakyThrows
    private boolean save(User user) {
        int x = statement.executeUpdate("insert into users\n" +
                " (name, UUID)\n" +
                "values ('"
                + user.getName() + "', '"
                + user.getUUID() + "');");
        return x == 1 ? true : false;
    }

    @SneakyThrows
    private boolean update(User user) {
        int x = statement.executeUpdate("update users set " +
                "name= '" + user.getName() + "', " +
                "UUID= '" + user.getUUID() + "', " +
                "WHERE id= " + user.getId() + ";");
        return x == 1 ? true : false;
    }



}
