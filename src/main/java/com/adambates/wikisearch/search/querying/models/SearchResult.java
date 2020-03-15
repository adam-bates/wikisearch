package com.adambates.wikisearch.search.querying.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResult {
    private final int id;
    private final float score;
    private final int wikiPageId;
    private final String wikiPageLink;
    private final String title;
    private final String content;
}
