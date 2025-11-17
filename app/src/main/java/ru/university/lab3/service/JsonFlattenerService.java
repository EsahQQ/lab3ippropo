package ru.university.lab3.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Сервис для преобразования вложенного JSON в плоскую структуру.
 */
public class JsonFlattenerService {

    /**
     * Рекурсивно преобразует вложенный JSON в плоскую Map.
     *
     * @param jsonNode JSON-объект для преобразования.
     * @return Плоская Map, где ключи - это пути в JSON (например, "user.address.city").
     */
    public Map<String, String> flatten(JsonNode jsonNode) {
        Map<String, String> flatMap = new LinkedHashMap<>();
        flattenNode("", jsonNode, flatMap);
        return flatMap;
    }

    private void flattenNode(String currentPath, JsonNode jsonNode, Map<String, String> flatMap) {
        if (jsonNode.isObject()) {
            String prefix = currentPath.isEmpty() ? "" : currentPath + ".";
            jsonNode.fields().forEachRemaining(entry ->
                    flattenNode(prefix + entry.getKey(), entry.getValue(), flatMap));
        } else if (jsonNode.isArray()) {
            String prefix = currentPath.isEmpty() ? "" : currentPath;
            for (int i = 0; i < jsonNode.size(); i++) {
                flattenNode(prefix + "[" + i + "]", jsonNode.get(i), flatMap);
            }
        } else {
            flatMap.put(currentPath, jsonNode.asText());
        }
    }

    /**
     * Преобразует Map обратно в JSON-объект.
     *
     * @param flatMap Плоская Map для преобразования.
     * @return JsonNode.
     */
    public JsonNode unflatten(Map<String, String> flatMap) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        flatMap.forEach((key, value) -> {
            root.put(key, value);
        });
        return root;
    }
}