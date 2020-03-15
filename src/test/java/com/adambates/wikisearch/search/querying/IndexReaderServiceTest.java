package com.adambates.wikisearch.search.querying;

import com.adambates.wikisearch.search.DocumentFieldName;
import com.adambates.wikisearch.search.querying.factories.IndexReaderFactory;
import com.adambates.wikisearch.search.querying.factories.IndexSearcherFactory;
import com.adambates.wikisearch.search.querying.models.SearchResult;
import com.adambates.wikisearch.search.querying.models.SearchResults;
import com.adambates.wikisearch.search.querying.models.TermResults;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IndexReaderServiceTest {

    private static final int RESULTS = 5;
    private static final String WIKI_PAGE_BASE_LINK = "https://en.wikipedia.org/wiki/";

    private final IndexReaderFactory indexReaderFactory = mock(IndexReaderFactory.class);
    private final IndexSearcherFactory indexSearcherFactory = mock(IndexSearcherFactory.class);

    private final IndexReaderService indexReaderService = new IndexReaderServiceImpl(indexReaderFactory, indexSearcherFactory, WIKI_PAGE_BASE_LINK);

    private final IndexReader indexReader = mock(IndexReader.class);
    private final IndexSearcher indexSearcher = mock(IndexSearcher.class);

    @BeforeEach
    void setUp() throws IOException {
        when(indexReaderFactory.createIndexReader()).thenReturn(indexReader);
        when(indexSearcherFactory.createIndexSearcher(indexReader)).then(invocationOnMock -> {
            // Fix closing NPE because .close() is final (not mockable)
            ReflectionTestUtils.setField(indexReader, "closed", true);
            return indexSearcher;
        });
        when(indexReader.getSumTotalTermFreq(anyString())).then(invocationOnMock -> {
            // Fix closing NPE because .close() is final (not mockable)
            ReflectionTestUtils.setField(indexReader, "closed", true);
            return 0L;
        });
    }

    @Test
    void getNSearchResultsForQuery() throws Exception {
        final Query query = mock(Query.class);
        final TopDocs topDocs = new TopDocs(null, new ScoreDoc[] {});
        when(indexSearcher.search(query, RESULTS)).thenReturn(topDocs);

        assertThat(indexReaderService.getNSearchResultsForQuery(query, RESULTS)).isEqualTo(SearchResults.builder()
                .pagesReturned(0)
                .pages(new ArrayList<>())
                .build());

        verify(indexReaderFactory).createIndexReader();
        verify(indexSearcherFactory).createIndexSearcher(indexReader);

        verify(indexSearcher).search(query, RESULTS);
    }

    @Test
    void getSearchResultById() throws Exception {
        final int id = 123;

        final Document document = new Document();
        document.add(new TextField(DocumentFieldName.PAGE_ID.getValue(), "1234", Field.Store.YES));
        document.add(new TextField(DocumentFieldName.NAME.getValue(), "name", Field.Store.YES));
        document.add(new TextField(DocumentFieldName.CONTENT.getValue(), "content", Field.Store.YES));

        when(indexSearcher.doc(id)).thenReturn(document);

        assertThat(indexReaderService.getSearchResultById(id)).isEqualTo(SearchResult.builder()
                .id(id)
                .score(1f)
                .wikiPageLink(WIKI_PAGE_BASE_LINK + "name")
                .wikiPageId(1234)
                .title("name")
                .content("content")
                .build());
    }

    @Test
    void getNTermResults() throws Exception {

        assertThat(indexReaderService.getNTermResults(0)).isEqualTo(TermResults.builder()
                .totalTermsIndexed(0)
                .terms(new ArrayList<>())
                .build());

        verify(indexReaderFactory).createIndexReader();
    }
}
