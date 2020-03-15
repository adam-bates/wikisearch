package com.adambates.wikisearch.search.indexing;

import com.adambates.wikisearch.search.indexing.factories.IndexWriterFactory;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import org.apache.lucene.index.IndexWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IndexWriterServiceTest {

    private final IndexWriterFactory indexWriterFactory = mock(IndexWriterFactory.class);
    private final IndexWriterService indexWriterService = new IndexWriterServiceImpl(indexWriterFactory);

    @Test
    void writeLoadedWikiPagesToIndex() throws IOException {
        final List<LoadedWikiPage> loadedWikiPages = buildLoadedWikiPages();
        final IndexWriter indexWriter = mock(IndexWriter.class);

        when(indexWriterFactory.createIndexWriter()).thenReturn(indexWriter);

        indexWriterService.writeLoadedWikiPagesToIndex(loadedWikiPages);

        verify(indexWriterFactory).createIndexWriter();
        verify(indexWriter).addDocuments(anyList());
        verify(indexWriter).close();
    }

    private static List<LoadedWikiPage> buildLoadedWikiPages() {
        return Collections.singletonList(LoadedWikiPage.builder()
                .id(1)
                .title("title")
                .content("content")
                .build());
    }
}
