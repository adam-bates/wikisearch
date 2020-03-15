package com.adambates.wikisearch.search.querying;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

public interface IndexReaderService {

    TopDocs getTopDocsForQuery(Query query, int n);
}
