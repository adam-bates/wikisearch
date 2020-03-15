package com.adambates.wikisearch.search.querying;

import com.adambates.wikisearch.search.querying.factories.IndexReaderFactory;
import com.adambates.wikisearch.search.querying.factories.IndexSearcherFactory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IndexReaderServiceTest {

    private static final int RESULTS = 5;

    private final IndexReaderFactory indexReaderFactory = mock(IndexReaderFactory.class);
    private final IndexSearcherFactory indexSearcherFactory = mock(IndexSearcherFactory.class);

    private final IndexReaderService indexReaderService = new IndexReaderServiceImpl(indexReaderFactory, indexSearcherFactory);

    private final IndexReader indexReader = mock(IndexReader.class);
    private final IndexSearcher indexSearcher = mock(IndexSearcher.class);

    @BeforeEach
    void setUp() throws IOException {
        when(indexReaderFactory.createIndexReader()).thenReturn(indexReader);
        when(indexSearcherFactory.createIndexSearcher(indexReader)).then(invocationOnMock -> {
            final IndexReader indexReader = invocationOnMock.getArgument(0);
            // Fix closing NPE because .close() is final (not mockable)
            ReflectionTestUtils.setField(indexReader, "closed", true);
            return indexSearcher;
        });
    }

    @Test
    void getTopDocsForQuery() throws Exception {
        final Query query = mock(Query.class);
        final TopDocs topDocs = mock(TopDocs.class);
        when(indexSearcher.search(query, RESULTS)).thenReturn(topDocs);

        assertThat(indexReaderService.getTopDocsForQuery(query, RESULTS)).isEqualTo(topDocs);

        verify(indexReaderFactory).createIndexReader();
        verify(indexSearcherFactory).createIndexSearcher(indexReader);

        verify(indexSearcher).search(query, RESULTS);
        verify(indexReader).close();
    }
}
