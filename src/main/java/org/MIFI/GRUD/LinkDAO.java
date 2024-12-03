package org.MIFI.GRUD;

import org.MIFI.utils.DataBaseUtils;
import org.MIFI.entity.Entity;
import org.MIFI.entity.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LinkDAO implements DAO {

    private Statement statement;

    public LinkDAO() {
        this.statement = DataBaseUtils.getInstance().getStmt();
    }

    public ArrayList<Link> findByUUID(String UUID) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM links WHERE UUID_user LIKE '" + UUID + "';");

            ArrayList<Link> links = new ArrayList<>();
            while (rs.next()) {
                Link link = new Link();
                link.setId((long) rs.getInt(1));
                link.setUUID(rs.getString(2));
                link.setLongLink(rs.getString(3));
                link.setShortLink(rs.getString(4));
                link.setDateStart((long) rs.getInt(5));
                link.setDateEnd((long) rs.getInt(6));
                link.setTransitionLimit(rs.getInt(7));
                links.add(link);
            }
            return links;
        } catch (SQLException e) {
            throw new RuntimeException(e);
//            return null;
        }
    }


    @Override
    public boolean save(Entity entity) {
        Link link = (Link) entity;
        int x = 0;
        try {
            x = statement.executeUpdate("insert into links\n" +
                    " (UUID_user, long_url, short_url, date_start, date_end, transition_limit)\n" +
                    "values ('"
                    + link.getUUID() + "', '"
                    + link.getLongLink() + "', '"
                    + link.getShortLink() + "', '"
                    + link.getDateStart() + "', '"
                    + link.getDateEnd() + "', '"
                    + link.getTransitionLimit() + "');");
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //            return false;
        }
        return x == 1 ? true : false;
    }

    public boolean deleteById(Long id) {
        try {
            int x = statement.executeUpdate("delete from links WHERE id = " + id + ";");
            return x == 1 ? true : false;
        } catch (SQLException e) {
            return false;
        }
    }
}
