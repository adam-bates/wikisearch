package com.adambates.wikisearch.controllers;

import com.adambates.wikisearch.controllers.models.ErrorResult;
import com.adambates.wikisearch.search.DocumentFieldName;
import com.adambates.wikisearch.search.querying.IndexReaderService;
import com.adambates.wikisearch.search.querying.models.SearchResults;
import com.adambates.wikisearch.search.querying.models.TermResults;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WikiQueryController {

    private final IndexReaderService indexReaderService;
    private final Analyzer analyzer;
    private final Gson gson;

    @GetMapping("/wiki/pages")
    public ResponseEntity<String> pages(@RequestParam(name = "query", defaultValue = "") final String queryValue,
                                        @RequestParam(name = "field", required = false, defaultValue = "content") final String field,
                                        @RequestParam(name = "limit", required = false, defaultValue = "10") String limitValue) throws Exception {
        final DocumentFieldName documentFieldName = DocumentFieldName.fromValue(field);
        if (documentFieldName == null) {
            return buildErrorResponse("Query param [field] is not valid");
        }

        final int limit = parsePositiveInteger(limitValue);
        if (limit <= 0) {
            return buildErrorResponse("Query param [limit] must be a positive integer");
        }

        final Query query = parseQueryString(documentFieldName, queryValue);
        final SearchResults searchResults = indexReaderService.getNSearchResultsForQuery(query, limit);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(searchResults));
    }

    @GetMapping("/wiki/terms")
    public ResponseEntity<String> terms(@RequestParam(name = "limit", required = false, defaultValue = "10") String limitValue) {
        final int limit = parsePositiveInteger(limitValue);
        if (limit <= 0) {
            return buildErrorResponse("Query param [limit] must be a positive integer");
        }

        final TermResults termResults = indexReaderService.getNTermResults(limit);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(termResults));
    }

    private Query parseQueryString(final DocumentFieldName documentFieldName, final String queryString) throws ParseException {
        if (StringUtils.isBlank(queryString)) {
            return new MatchAllDocsQuery();
        } else {
            return new QueryParser(documentFieldName.getValue(), analyzer).parse(queryString);
        }
    }

    private ResponseEntity<String> buildErrorResponse(final String errorMessage) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(ErrorResult.builder()
                        .message(errorMessage)
                        .build()));
    }

    private static int parsePositiveInteger(final String value) {
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            return -1;
        }
    }
}
