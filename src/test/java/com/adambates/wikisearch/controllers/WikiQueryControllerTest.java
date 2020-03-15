package com.adambates.wikisearch.controllers;

import com.adambates.wikisearch.search.DocumentFieldName;
import com.adambates.wikisearch.search.querying.IndexReaderService;
import com.adambates.wikisearch.search.querying.models.SearchResult;
import com.adambates.wikisearch.search.querying.models.SearchResults;
import com.google.gson.Gson;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WikiQueryControllerTest {

    private final IndexReaderService indexReaderService = mock(IndexReaderService.class);
    private final Analyzer analyzer = mock(Analyzer.class);
    private final Gson gson = new Gson();

    private final WikiQueryController controller = new WikiQueryController(indexReaderService, analyzer, gson);

    @Test
    void pages() {
        final String query = "";
        final String field = DocumentFieldName.CONTENT.getValue();
        final int limit = 3;

        final SearchResults searchResults = SearchResults.builder()
                .pagesReturned(1)
                .pages(Collections.singletonList(SearchResult.builder()
                        .id(123)
                        .score(1f)
                        .wikiPageLink("link")
                        .wikiPageId(321)
                        .title("title")
                        .content("content")
                        .build()))
                .build();

        when(indexReaderService.getNSearchResultsForQuery(any(Query.class), eq(limit))).thenReturn(searchResults);

        final ResponseEntity<String> response = controller.pages(query, field, String.valueOf(limit));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(new Gson().fromJson(response.getBody(), SearchResults.class)).isEqualTo(searchResults);
    }

    @Test
    void page() {
        final int id = 123;
        final SearchResult searchResult = SearchResult.builder()
                .id(id)
                .score(1f)
                .wikiPageLink("link")
                .wikiPageId(321)
                .title("title")
                .content("content")
                .build();

        when(indexReaderService.getSearchResultById(id)).thenReturn(searchResult);

        final ResponseEntity<String> response = controller.page(String.valueOf(id));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(new Gson().fromJson(response.getBody(), SearchResult.class)).isEqualTo(searchResult);
    }

    @Test
    void terms() {
        final String limit = "3";

        final ResponseEntity<String> response = controller.terms(limit);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();
    }
}
