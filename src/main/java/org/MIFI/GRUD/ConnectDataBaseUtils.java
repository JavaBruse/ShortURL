package org.MIFI.GRUD;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Data
public class ConnectDataBaseUtils {
    private Connection connection;
    private Statement stmt;
    private static volatile ConnectDataBaseUtils instance;

    public static ConnectDataBaseUtils getInstance() {
        ConnectDataBaseUtils localInstance = instance;
        if (localInstance == null) {
            synchronized (ConnectDataBaseUtils.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConnectDataBaseUtils();
                }
            }
        }
        return localInstance;
    }

    @SneakyThrows
    public void connect() {
        String fileName = "service.db";
        boolean check = checkFile(fileName);
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        stmt = connection.createStatement();
        if (!check) createTable();
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void createTable() {
        stmt.execute("CREATE TABLE users (\n" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    name      TEXT        NOT NULL\n" +
                "                          UNIQUE,\n" +
                "    UUID      TEXT        NOT NULL\n" +
                "                          UNIQUE\n" +
                ");");

        stmt.execute("CREATE TABLE links (\n" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    id_user   INTEGER     NOT NULL,\n" +
                "    long      TEXT        NOT NULL,\n" +
                "    short     TEXT        NOT NULL\n" +
                "                          UNIQUE,\n" +
                "    FOREIGN KEY (id_user)\n" +
                "    REFERENCES users (id) ON DELETE CASCADE\n" +
                ");");

        stmt.execute("CREATE TABLE properties_link (\n" +
                "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    id_link     INTEGER     NOT NULL\n" +
                "                          UNIQUE,\n" +
                "    date_start  DATE        NOT NULL,\n" +
                "    date_end   DATE        NOT NULL,\n" +
                "    transition_limit       INTEGER     NOT NULL,\n" +
                "    FOREIGN KEY (id_link)\n" +
                "    REFERENCES links (id) ON DELETE CASCADE\n" +
                ");");
    }

    private boolean checkFile(String fileCheck) {
        File file = new File(fileCheck);
        return file.exists();
    }

    private void createFolder(String folderName){
        File folder = new File(folderName);
        if (!folder.exists()) folder.mkdir();
    }
}
