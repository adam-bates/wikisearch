package com.adambates.wikisearch.search.querying.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResults {
    private final int pagesReturned;
    private final List<SearchResult> pages;
}
