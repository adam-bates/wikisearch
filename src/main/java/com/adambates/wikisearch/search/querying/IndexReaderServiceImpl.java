package com.adambates.wikisearch.search.querying;

import com.adambates.wikisearch.search.querying.factories.IndexReaderFactory;
import com.adambates.wikisearch.search.querying.factories.IndexSearcherFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
class IndexReaderServiceImpl implements IndexReaderService {

    private final IndexReaderFactory indexReaderFactory;
    private final IndexSearcherFactory indexSearcherFactory;

    @Override
    public TopDocs getTopDocsForQuery(final Query query, final int n) {
        try (final IndexReader reader = indexReaderFactory.createIndexReader()) {
            return indexSearcherFactory.createIndexSearcher(reader).search(query, n);
        } catch (final IOException e) {
            log.error("Unable to read index for query: ", e);
            throw new RuntimeException(e);
        }
    }
}
