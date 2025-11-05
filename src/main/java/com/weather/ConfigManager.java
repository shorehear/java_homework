package com.weather;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Попытка загрузить из текущей директории
                try (FileInputStream fileInput = new FileInputStream(CONFIG_FILE)) {
                    properties.load(fileInput);
                }
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке файла конфигурации: " + e.getMessage());
        }
    }

    public String getApiKey() {
        return properties.getProperty("api.key");
    }

    public String getApiUrl() {
        return properties.getProperty("api.url", 
                "https://api.openweathermap.org/data/2.5/weather");
    }
}
