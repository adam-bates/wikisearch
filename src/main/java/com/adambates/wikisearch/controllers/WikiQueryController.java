package com.adambates.wikisearch.controllers;

import com.adambates.wikisearch.controllers.models.ErrorResult;
import com.adambates.wikisearch.search.DocumentFieldName;
import com.adambates.wikisearch.search.querying.IndexReaderService;
import com.adambates.wikisearch.search.querying.models.SearchResults;
import com.adambates.wikisearch.search.querying.models.TermResults;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Controller to query indexed wiki results.
 */
@Slf4j
@RestController
@AllArgsConstructor
public class WikiQueryController {

    private final IndexReaderService indexReaderService;
    private final Analyzer analyzer;
    private final Gson gson;

    /**
     * GET request to return queried wiki pages from what's been indexed and stored.
     *
     * @param queryValue String representing the search query. See https://lucene.apache.org/core/2_9_4/queryparsersyntax.html for more details on syntax
     * @param field String a valid {@link DocumentFieldName} value representing the data-field to run the search on
     * @param limitValue String positive integer representing the maximum number of results wanted
     * @return ResponseEntity JSON representation of SearchResults (or ErrorResult if there was an issue)
     */
    @GetMapping("/wiki/pages")
    public ResponseEntity<String> pages(@RequestParam(name = "query", required = false, defaultValue = "") final String queryValue,
                                        @RequestParam(name = "field", required = false, defaultValue = "content") final String field,
                                        @RequestParam(name = "limit", required = false, defaultValue = "10") String limitValue) {
        final DocumentFieldName documentFieldName = DocumentFieldName.fromValue(field);
        if (documentFieldName == null) {
            return buildErrorResponse("Query param [field] must be one of the following: " + DocumentFieldName.acceptedValues());
        }

        final int limit = parseInteger(limitValue);
        if (limit <= 0) {
            return buildErrorResponse("Query param [limit] must be a positive integer");
        }

        final Query query = parseQueryString(documentFieldName, queryValue);
        if (query == null) {
            return buildErrorResponse("The requested query [" + queryValue + "] is not valid." +
                    " Please see https://lucene.apache.org/core/2_9_4/queryparsersyntax.html for more details on query syntax.");
        }

        final SearchResults searchResults = indexReaderService.getNSearchResultsForQuery(query, limit);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(searchResults));
    }

    @GetMapping("/wiki/terms")
    public ResponseEntity<String> terms(@RequestParam(name = "limit", required = false, defaultValue = "10") String limitValue) {
        final int limit = parseInteger(limitValue);
        if (limit <= 0) {
            return buildErrorResponse("Query param [limit] must be a positive integer");
        }

        final TermResults termResults = indexReaderService.getNTermResults(limit);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(termResults));
    }

    private Query parseQueryString(final DocumentFieldName documentFieldName, final String queryString) {
        if (StringUtils.isBlank(queryString)) {
            return new MatchAllDocsQuery();
        } else {
            try {
                return new QueryParser(documentFieldName.getValue(), analyzer).parse(queryString);
            } catch (final ParseException e) {
                log.error("Couldn't parse query: ", e);
                return null;
            }
        }
    }

    private ResponseEntity<String> buildErrorResponse(final String errorMessage) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(ErrorResult.builder()
                        .message(errorMessage)
                        .build()));
    }

    private static int parseInteger(final String value) {
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            log.error("Couldn't parse integer: ", e);
            return -1;
        }
    }
}
