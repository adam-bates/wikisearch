package com.adambates.wikisearch.controllers;

import com.adambates.wikisearch.search.DocumentFieldName;
import com.adambates.wikisearch.search.querying.IndexReaderService;
import com.google.gson.Gson;
import org.apache.lucene.analysis.Analyzer;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class WikiQueryControllerTest {

    private final IndexReaderService indexReaderService = mock(IndexReaderService.class);
    private final Analyzer analyzer = mock(Analyzer.class);
    private final Gson gson = new Gson();

    private final WikiQueryController controller = new WikiQueryController(indexReaderService, analyzer, gson);

    @Test
    void pages() throws Exception {
        final String query = "";
        final String field = DocumentFieldName.CONTENT.getValue();
        final String limit = "3";

        final ResponseEntity<String> response = controller.pages(query, field, limit);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();
    }

    @Test
    void terms() {
        final String limit = "3";

        final ResponseEntity<String> response = controller.terms(limit);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();
    }
}
