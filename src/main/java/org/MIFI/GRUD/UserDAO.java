package org.MIFI.GRUD;

import org.MIFI.utils.DataBaseUtils;
import org.MIFI.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO implements DAO {

    private Statement statement;

    public UserDAO() {
        this.statement = DataBaseUtils.getInstance().getStmt();
    }

    public User findByUUID(String UUID) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE UUID LIKE '" + UUID + "';");
            User user = new User();
            user.setUUID(rs.getString(1));
            user.setName(rs.getString(2));
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean deleteByUUID(String UUID) {
        try {
            int x = statement.executeUpdate("delete from users WHERE UUID = '" + UUID + "';");
            return x == 1 ? true : false;
        } catch (SQLException e) {
            return false;
        }
    }

    public User save(User user) {
        int x = 0;
        try {
            statement.executeUpdate("insert into users\n" +
                    " (UUID, name)\n" +
                    "values ('"
                    + user.getUUID() + "', '"
                    + user.getName() + "');");
        } catch (SQLException e) {
                return null;
        }
        return user;
    }
}
