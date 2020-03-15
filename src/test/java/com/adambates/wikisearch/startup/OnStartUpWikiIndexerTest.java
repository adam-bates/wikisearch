package com.adambates.wikisearch.startup;

import com.adambates.wikisearch.wiki.services.WikiService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OnStartUpWikiIndexerTest {

    private static final int PAGES_TO_LOAD = 10;

    private final WikiService wikiService = mock(WikiService.class);

    private final OnStartUpWikiIndexer onStartUpWikiIndexer = new OnStartUpWikiIndexer(wikiService, PAGES_TO_LOAD);

    @Test
    void onApplicationEvent() {
        onStartUpWikiIndexer.onApplicationEvent(null);

        verify(wikiService).loadNRandomWikiPages(PAGES_TO_LOAD);
    }
}
