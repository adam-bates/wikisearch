package com.adambates.wikisearch.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TextFieldName {
    NAME("name"),
    CONTENT("content"),
    ;

    private final String value;
}
