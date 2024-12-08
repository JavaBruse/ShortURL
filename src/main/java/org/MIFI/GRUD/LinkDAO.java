package org.MIFI.GRUD;

import org.MIFI.utils.DataBaseUtils;
import org.MIFI.entity.Entity;
import org.MIFI.entity.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

public class LinkDAO implements DAO {

    private Statement statement;

    public LinkDAO() {
        this.statement = DataBaseUtils.getInstance().getStmt();
    }

    public Optional<ArrayList<Link>> findByUUID(String UUID) {
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
            if (links.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(links);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }


    public boolean saveLink(Link link) {
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

    public boolean updateLink(Link link) {
        int x = 0;
        try {
            x = statement.executeUpdate("update links set " +
                    "UUID_user= '" + link.getUUID() + "', " +
                    "long_url= '" + link.getLongLink() + "', " +
                    "short_url= '" + link.getShortLink() + "', " +
                    "date_start= '" + link.getDateStart() + "', " +
                    "date_end= '" + link.getDateEnd() + "', " +
                    "transition_limit= '" + link.getTransitionLimit() + "' " +
                    "WHERE short_url= '" + link.getShortLink() + "';");
            return x == 1 ? true : false;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteById(Long id) {
        try {
            int x = statement.executeUpdate("delete from links WHERE id = " + id + ";");
            return x == 1 ? true : false;
        } catch (SQLException e) {
            return false;
        }
    }

    public Optional<Link> findLongByShortLink(String shortLink) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM links WHERE short_url LIKE '" + shortLink + "';");
            if (rs.next()) { // Проверяем, есть ли записи в ResultSet
                Link link = new Link();
                link.setId((long) rs.getInt(1));
                link.setUUID(rs.getString(2));
                link.setLongLink(rs.getString(3));
                link.setShortLink(rs.getString(4));
                link.setDateStart(rs.getLong(5));
                link.setDateEnd(rs.getLong(6));
                link.setTransitionLimit(rs.getInt(7));
                return Optional.of(link);
            }
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
