package com.sabina.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public interface JsonTransform {
    default void filterNode(JsonNode node, Set<String> requiredFields,
                    String parentPath) {

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();

        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> mapEntry = fieldsIterator.next();
            String entryKey = mapEntry.getKey();
            JsonNode entryValue = mapEntry.getValue();
            String fieldPath;
            if (!parentPath.isEmpty()) {
                fieldPath = parentPath + "." + entryKey;
            } else {
                fieldPath = entryKey;
            }

            boolean contains = false;
            for (String field: requiredFields) {
                if (field.equals(fieldPath) || field.startsWith(fieldPath + ".")) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {

                fieldsIterator.remove();

            } else {
                if (entryValue.isObject()) {
                    filterNode(entryValue, requiredFields, fieldPath);
                } else if (entryValue.isArray()) {
                    for (JsonNode arrayElementNode : (ArrayNode) entryValue) {
                        filterNode(arrayElementNode, requiredFields, fieldPath);
                    }
                }

            }

        }
    }

    default void nodeToMap(String currentPath, JsonNode jsonNode, Map<String,String> fieldsMapping, Map<String, String> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                nodeToMap(pathPrefix + entry.getKey(), entry.getValue(), fieldsMapping, map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                nodeToMap(currentPath + "[" + i + "]", arrayNode.get(i), fieldsMapping, map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(fieldsMapping.get(currentPath), valueNode.asText());
        }
    }
}
