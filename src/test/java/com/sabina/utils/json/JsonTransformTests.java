package com.sabina.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JsonTransformTests implements JsonTransform {

    private JsonNode node = null;
    private static ObjectMapper mapper = new ObjectMapper();
    private static ResourceBundle mappingsBundle = ResourceBundle.getBundle("mappingConfig");

    @Before
    public void setup() throws IOException {
        InputStream exampleInput = JsonTransformTests.class.getClassLoader()
                .getResourceAsStream("example.json");
        node = mapper.readTree(exampleInput);
        ResourceBundle mappingsBundle = ResourceBundle.getBundle("mappingConfig");
    }

    @Test
    public void testFilterAndFlatten() throws JsonProcessingException {
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> keys = mappingsBundle.getKeys();
        Map<String,String> fieldsMapping = new HashMap();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            fieldsMapping.put(key, mappingsBundle.getString(key));
        }
        filterNode(node,fieldsMapping.keySet(),"");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        nodeToMap("", node, fieldsMapping, map);
        System.out.println(map);
    }
}
