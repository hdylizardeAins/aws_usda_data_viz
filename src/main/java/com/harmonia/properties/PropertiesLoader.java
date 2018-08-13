package com.harmonia.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {

    private final Properties properties;

    /**
     * Load the properties file
     *
     * @param propertiesFile The file path of the file to load, relative to the classpath
     * @throws RuntimeException if the file doesn't exist
     */
    public PropertiesLoader(String propertiesFile) {
        ClassLoader classLoader = getClass().getClassLoader();
        properties = new Properties();
        try (InputStream inputStream = classLoader.getResourceAsStream(propertiesFile)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties file " + propertiesFile, e);
        }
    }

    /**
     * Retrieve a property value given a property name
     *
     * @param propertyName The name of the property to search for
     * @return The property, or {@code null} if it was not found
     */
    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
