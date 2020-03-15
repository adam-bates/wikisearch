package com.adambates.wikisearch.json.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilsTest {

    private static final UUID UUID = java.util.UUID.randomUUID();

    private static final String TEST_KEY = "testKey";

    private static final String TEST_VALUE_1 = "testValue1";
    private static final String TEST_VALUE_2 = "testValue2";

    private JsonObject jsonObject1;
    private JsonObject jsonObject2;
    private JsonArray jsonArray;

    @BeforeEach
    void setUp() {
        jsonObject1 = new JsonObject();
        jsonObject1.addProperty(TEST_KEY, TEST_VALUE_1);

        jsonObject2 = new JsonObject();
        jsonObject2.addProperty(TEST_KEY, TEST_VALUE_2);

        jsonArray = new JsonArray();
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
    }

    @Test
    void buildListFromJsonArray() {
        final List<String> result = JsonUtils.buildListFromJsonArray(jsonArray,
                jsonObject -> jsonObject.get(TEST_KEY).getAsString() + UUID);

        assertThat(result).containsExactly(
                TEST_VALUE_1 + UUID,
                TEST_VALUE_2 + UUID
        );
    }
}
