package com.adambates.wikisearch.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentFieldName {
    PAGE_ID("pageId"),
    NAME("name"),
    CONTENT("content"),
    ;

    private final String value;

    public static DocumentFieldName fromValue(final String value) {
        return Arrays.stream(values())
                .filter(documentFieldName -> StringUtils.equalsIgnoreCase(documentFieldName.value, value))
                .findAny()
                .orElse(null);
    }
}
