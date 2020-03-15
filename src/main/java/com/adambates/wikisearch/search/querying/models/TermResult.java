package com.adambates.wikisearch.search.querying.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TermResult {
    private final long totalFrequency;
    private final int wikiPageFrequency;
    private final String term;
}
