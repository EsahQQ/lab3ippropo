package ru.university.lab3.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Сервис для загрузки конфигурации из .properties файла.
 */
public class ConfigLoader {
    private final Properties properties = new Properties();

    /**
     * Загружает конфигурацию из файла.
     * @param path Путь к файлу app.properties.
     * @throws IOException Если файл не найден или не удалось прочитать.
     */
    public void load(String path) throws IOException {
        try (FileReader reader = new FileReader(path)) {
            properties.load(reader);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}