package com.adambates.wikisearch.search;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public final class DocumentFieldName {

    private static final Map<String, DocumentFieldName> VALUE_MAP = new HashMap<>();

    public static DocumentFieldName
            PAGE_ID = new DocumentFieldName("id"),
            NAME = new DocumentFieldName("name"),
            CONTENT = new DocumentFieldName("content");

    private final String value;

    private DocumentFieldName(final String value) {
        this.value = value;
        VALUE_MAP.put(normalizedValue(value), this);
    }

    public static DocumentFieldName fromValue(final String value) {
        return VALUE_MAP.get(normalizedValue(value));
    }

    public static Set<String> acceptedValues() {
        return VALUE_MAP.keySet();
    }

    private static String normalizedValue(final String value) {
        return value.toLowerCase();
    }
}
