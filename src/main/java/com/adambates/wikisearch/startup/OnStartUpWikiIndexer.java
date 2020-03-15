package com.adambates.wikisearch.startup;

import com.adambates.wikisearch.search.indexing.IndexWriterService;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import com.adambates.wikisearch.wiki.services.WikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class OnStartUpWikiIndexer {

    private final WikiService wikiService;
    private final IndexWriterService indexWriterService;
    private final int pagesToLoad;

    OnStartUpWikiIndexer(final WikiService wikiService,
                         final IndexWriterService indexWriterService,
                         @Value("${startup.wiki.pages}") final int pagesToLoad) {
        this.wikiService = wikiService;
        this.indexWriterService = indexWriterService;
        this.pagesToLoad = pagesToLoad;
    }

    @EventListener
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final List<LoadedWikiPage> loadedWikiPages = wikiService.loadNRandomWikiPages(pagesToLoad);
        indexWriterService.writeLoadedWikiPagesToIndex(loadedWikiPages);
    }
}
