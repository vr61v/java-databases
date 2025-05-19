package com.vr61v.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesManager {

    private static final Properties PROPERTIES = new Properties();

    private PropertiesManager() {}

    static { loadProperties(); }
    private static void loadProperties() {
        try (InputStream inputStream = PropertiesManager.class
                .getClassLoader()
                .getResourceAsStream("application.properties")
        ) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

}
