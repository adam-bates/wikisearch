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

        log.info("\n\n" +
                "****************************************\n" +
                "***                                  ***\n" +
                "***      Adam Bates' WikiSearch      ***\n" +
                "***                                  ***\n" +
                "****************************************\n" +
                "\n" +
                "Welcome! I've downloaded and indexed {} wiki pages to be searched using the exposed API.\n" +
                "\n" +
                "Wiki Pages URI: http://localhost:8080/wiki/pages\n" +
                "Wiki Terms URI: http://localhost:8080/wiki/terms\n" +
                "\n" +
                "For more information on how to use the APIs see the project-level README.md file,\n" +
                "or head on over to the GitHub repo: https://github.com/adam-bates/wikisearch\n\n",
                loadedWikiPages.size()
        );
    }
}
