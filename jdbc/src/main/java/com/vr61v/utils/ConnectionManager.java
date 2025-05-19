package com.vr61v.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String URL = PropertiesManager.getProperty("database.url");
    private static final String USER = PropertiesManager.getProperty("database.user");
    private static final String PASSWORD = PropertiesManager.getProperty("database.password");

    private ConnectionManager() {}

    public static Connection open() throws RuntimeException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
