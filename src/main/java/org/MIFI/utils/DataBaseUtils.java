package org.MIFI.utils;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class DataBaseUtils {
    private Connection connection;
    private Statement stmt;
    private static volatile DataBaseUtils instance;

    public static DataBaseUtils getInstance() {
        DataBaseUtils localInstance = instance;
        if (localInstance == null) {
            synchronized (DataBaseUtils.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataBaseUtils();
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
        stmt.execute("CREATE TABLE users (" +
                "    UUID TEXT PRIMARY KEY NOT NULL, " +
                "    name TEXT NOT NULL" +
                ");");
        stmt.execute("CREATE TABLE links (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "    UUID_user TEXT NOT NULL, " +
                "    long_url TEXT NOT NULL, " +
                "    short_url TEXT UNIQUE NOT NULL, " +
                "    date_start INTEGER NOT NULL, " +
                "    date_end INTEGER NOT NULL, " +
                "    transition_limit INTEGER NOT NULL, " +
                "    FOREIGN KEY (UUID_user) REFERENCES users (UUID) ON DELETE CASCADE" +
                ");");
    }

    private boolean checkFile(String fileCheck) {
        File file = new File(fileCheck);
        return file.exists();
    }
}
