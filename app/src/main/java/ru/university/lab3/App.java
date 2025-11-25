package ru.university.lab3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.university.lab3.config.ConfigLoader;
import ru.university.lab3.service.JsonFlattenerService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        // Чтение путей из переменных окружения
        String inputJsonPath = System.getenv("INPUT_JSON");
        if (inputJsonPath == null) {
            System.err.println("Error: Environment variable INPUT_JSON is not set.");
            return;
        }
        // Загрузка конфигурации
        ConfigLoader config = new ConfigLoader();
        config.load("app.properties");
        String searchKey = config.getProperty("search.key");
        String filterKey = config.getProperty("filter.key");
        String filterValue = config.getProperty("filter.value");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(inputJsonPath));
        JsonFlattenerService flattener = new JsonFlattenerService();

        System.out.println("Task 1: Flatten JSON");
        if (rootNode.isArray()) {
            System.out.println("Input is an array, flattening each element:");
            for (JsonNode element : rootNode) {
                Map<String, String> flatJson = flattener.flatten(element);
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flatJson));
            }
        } else {
            Map<String, String> flatJson = flattener.flatten(rootNode);
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flatJson));
        }

        System.out.println("\nTask 2: Search by key '" + searchKey);
        Map<String, String> flatJsonForSearch = flattener.flatten(rootNode);
        if (flatJsonForSearch.containsKey(searchKey)) {
            System.out.println("Found value: " + flatJsonForSearch.get(searchKey));
        } else {
            System.out.println("Key not found.");
        }

        System.out.println("\nTask 3: Filter array by '" + filterKey + "==" + filterValue);
        if (rootNode.isArray()) {
            List<Map<String, String>> filteredResults = new ArrayList<>();

            for (JsonNode elementNode : rootNode) {
                Map<String, String> flatElement = flattener.flatten(elementNode);

                if (flatElement.containsKey(filterKey) && flatElement.get(filterKey).equals(filterValue)) {
                    filteredResults.add(flatElement);
                }
            }

            System.out.println("Filtered elements (flattened):");
            for (Map<String, String> result : filteredResults) {
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
            }

        } else {
            System.out.println("Input is not an array, skipping filter task.");
        }
    }
}