package com.vr61v.utils;

import com.vr61v.exceptions.ConnectionException;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class ConnectionManager {

    private static final String URL = PropertiesManager.getProperty("database.url");
    private static final String USER = PropertiesManager.getProperty("database.user");
    private static final String PASSWORD = PropertiesManager.getProperty("database.password");
    private static final String POOL_SIZE = PropertiesManager.getProperty("database.pool.size");
    private static final int DEFAULT_POOL_SIZE = 5;

    private static BlockingQueue<Connection> connections;

    static {
        initConnectionPool();
    }

    private static Connection open() throws RuntimeException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return (Connection) Proxy.newProxyInstance(
                    ConnectionManager.class.getClassLoader(),
                    new Class[] {Connection.class},
                    ((proxy, method, args) ->
                            method.getName().equals("close") ?
                                    connections.add((Connection) proxy) :
                                    method.invoke(connection, args)
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() {
        int size = POOL_SIZE == null ? DEFAULT_POOL_SIZE : Integer.parseInt(POOL_SIZE);
        connections = new LinkedBlockingQueue<>(size);

        for (int i = 0; i < size; ++i) {
            connections.add(open());
        }
    }

    private ConnectionManager() {}

    public static Connection getConnection() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

}
