package ru.university.lab3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.university.lab3.service.JsonFlattenerService;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonFlattenerServiceTest {
    private JsonFlattenerService flattener;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        flattener = new JsonFlattenerService();
        mapper = new ObjectMapper();
    }

    @Test
    void testSimpleFlatten() throws IOException {
        String json = "{\"a\": 1, \"b\": \"hello\"}";
        JsonNode node = mapper.readTree(json);
        Map<String, String> flatMap = flattener.flatten(node);

        assertEquals("1", flatMap.get("a"));
        assertEquals("hello", flatMap.get("b"));
        assertEquals(2, flatMap.size());
    }

    @Test
    void testNestedFlatten() throws IOException {
        String json = "{\"user\":{\"name\":\"Alice\",\"address\":{\"city\":\"New York\"}}}";
        JsonNode node = mapper.readTree(json);
        Map<String, String> flatMap = flattener.flatten(node);

        assertEquals("Alice", flatMap.get("user.name"));
        assertEquals("New York", flatMap.get("user.address.city"));
        assertEquals(2, flatMap.size());
    }

    @Test
    void testArrayFlatten() throws IOException {
        String json = "{\"items\":[\"a\", {\"key\":\"value\"}]}";
        JsonNode node = mapper.readTree(json);
        Map<String, String> flatMap = flattener.flatten(node);

        assertEquals("a", flatMap.get("items[0]"));
        assertEquals("value", flatMap.get("items[1].key"));
        assertEquals(2, flatMap.size());
    }
}