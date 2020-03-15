package com.adambates.wikisearch.json.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class JsonUtils {

    public <T> List<T> buildListFromJsonArray(final JsonArray jsonArray,
                                              final Function<JsonObject, T> elementToObjectFunction) {
        final List<T> list = new ArrayList<>();
        jsonArray.forEach(jsonElement -> list.add(elementToObjectFunction.apply(jsonElement.getAsJsonObject())));
        return list;
    }
}
