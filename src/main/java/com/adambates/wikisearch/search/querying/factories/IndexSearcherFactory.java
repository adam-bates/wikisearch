package com.adambates.wikisearch.search.querying.factories;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.springframework.stereotype.Component;

@Component
public class IndexSearcherFactory {

    public IndexSearcher createIndexSearcher(final IndexReader indexReader) {
        return new IndexSearcher(indexReader);
    }
}
