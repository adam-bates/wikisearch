package com.adambates.wikisearch.startup;

import com.adambates.wikisearch.wiki.services.WikiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class OnStartUpWikiIndexer {

    private final WikiService wikiService;
    private final int pagesToLoad;

    OnStartUpWikiIndexer(final WikiService wikiService,
                         @Value("${startup.wiki.pages}") final int pagesToLoad) {
        this.wikiService = wikiService;
        this.pagesToLoad = pagesToLoad;
    }

    @EventListener
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        wikiService.loadNRandomWikiPages(pagesToLoad);
    }
}
