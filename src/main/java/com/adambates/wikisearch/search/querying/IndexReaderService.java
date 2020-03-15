package com.adambates.wikisearch.search.querying;

import com.adambates.wikisearch.search.querying.models.SearchResults;
import com.adambates.wikisearch.search.querying.models.TermResults;
import org.apache.lucene.search.Query;

public interface IndexReaderService {

    SearchResults getNSearchResultsForQuery(Query query, int n);
    TermResults getNTermResults(int n);
}
