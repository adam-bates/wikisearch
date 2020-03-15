package com.adambates.wikisearch.startup;

import com.adambates.wikisearch.search.indexing.IndexWriterService;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import com.adambates.wikisearch.wiki.services.WikiService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OnStartUpWikiIndexerTest {

    private static final int PAGES_TO_LOAD = 10;

    private final WikiService wikiService = mock(WikiService.class);
    private final IndexWriterService indexWriterService = mock(IndexWriterService.class);

    private final OnStartUpWikiIndexer onStartUpWikiIndexer = new OnStartUpWikiIndexer(wikiService, indexWriterService, PAGES_TO_LOAD);

    @Test
    void onApplicationEvent() {
        final List<LoadedWikiPage> loadedWikiPages = buildLoadedWikiPages();

        when(wikiService.loadNRandomWikiPages(PAGES_TO_LOAD)).thenReturn(loadedWikiPages);

        onStartUpWikiIndexer.onApplicationEvent(null);

        verify(wikiService).loadNRandomWikiPages(PAGES_TO_LOAD);
        verify(indexWriterService).writeLoadedWikiPagesToIndex(loadedWikiPages);
    }

    private static List<LoadedWikiPage> buildLoadedWikiPages() {
        return Collections.singletonList(LoadedWikiPage.builder()
                .id(1)
                .title("title")
                .content("content")
                .build());
    }
}
