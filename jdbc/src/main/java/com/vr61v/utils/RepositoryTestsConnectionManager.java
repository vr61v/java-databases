package com.vr61v.utils;

import java.util.concurrent.LinkedBlockingQueue;

public class RepositoryTestsConnectionManager extends RepositoryConnectionManager {

    public RepositoryTestsConnectionManager() {
        this.URL = PropertiesManager.getProperty("database.tests.url");
        this.USER = PropertiesManager.getProperty("database.tests.user");
        this.PASSWORD = PropertiesManager.getProperty("database.tests.password");
        this.POOL_SIZE = PropertiesManager.getProperty("database.tests.pool.size");
        this.DEFAULT_POOL_SIZE = 5;

        int size = POOL_SIZE == null ? DEFAULT_POOL_SIZE : Integer.parseInt(POOL_SIZE);
        connections = new LinkedBlockingQueue<>(size);

        for (int i = 0; i < size; ++i) {
            connections.add(open());
        }
    }

}
