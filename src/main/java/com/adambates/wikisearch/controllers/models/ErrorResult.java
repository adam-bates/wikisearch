package com.adambates.wikisearch.controllers.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResult {
    private final String message;
}
