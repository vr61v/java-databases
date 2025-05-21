package com.vr61v.utils;

import com.vr61v.exceptions.ConnectionException;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RepositoryConnectionManager implements ConnectionManager {

    protected String URL;
    protected String USER;
    protected String PASSWORD;
    protected String POOL_SIZE;
    protected int DEFAULT_POOL_SIZE;

    protected BlockingQueue<Connection> connections;

    public RepositoryConnectionManager() {
        this.URL = PropertiesManager.getProperty("database.url");
        this.USER = PropertiesManager.getProperty("database.user");
        this.PASSWORD = PropertiesManager.getProperty("database.password");
        this.POOL_SIZE = PropertiesManager.getProperty("database.pool.size");
        this.DEFAULT_POOL_SIZE = 5;

        int size = POOL_SIZE == null ? DEFAULT_POOL_SIZE : Integer.parseInt(POOL_SIZE);
        connections = new LinkedBlockingQueue<>(size);

        for (int i = 0; i < size; ++i) {
            connections.add(open());
        }
    }

    protected Connection open() throws RuntimeException {
        try {
            Connection connection = DriverManager.getConnection(this.URL, this.USER, this.PASSWORD);
            return (Connection) Proxy.newProxyInstance(
                    RepositoryConnectionManager.class.getClassLoader(),
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

    public Connection getConnection() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

}
