package com.adambates.wikisearch.wiki.services;

import com.adambates.wikisearch.wiki.gateway.WikiGateway;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import com.adambates.wikisearch.wiki.models.WikiPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
class WikiServiceImpl implements WikiService {

    private final WikiGateway wikiGateway;

    @Override
    public List<LoadedWikiPage> loadNRandomWikiPages(final int n) {
        final List<WikiPage> wikiPages = getNRandomWikiPages(n);
        return loadWikiPages(wikiPages);
    }

    private List<WikiPage> getNRandomWikiPages(final int n) {
        log.info("Requesting [{}] random wiki pages", n);

        final List<WikiPage> wikiPages = wikiGateway.queryNRandomWikiPages(n);

        if (log.isDebugEnabled()) { // Don't create loadedIds list if not logging DEBUG
            final List<Integer> loadedIds = wikiPages.stream()
                    .map(WikiPage::getId)
                    .collect(Collectors.toList());

            log.debug("Actually got [{}] random wiki pages with ids: {}", wikiPages.size(), loadedIds);
        }

        return wikiPages;
    }

    private List<LoadedWikiPage> loadWikiPages(final List<WikiPage> wikiPages) {
        log.info("Requesting extracted content for [{}] wiki pages", wikiPages.size());

        final List<LoadedWikiPage> loadedWikiPages = wikiPages.stream()
                .map(wikiGateway::queryWikiPageForExtractedContents)
                .collect(Collectors.toList());

        if (log.isDebugEnabled()) { // Don't create loadedIds list if not logging DEBUG
            final List<Integer> loadedIds = loadedWikiPages.stream()
                    .map(LoadedWikiPage::getId)
                    .collect(Collectors.toList());

            log.debug("Actually extracted [{}] wiki pages' content with ids: {}", loadedWikiPages.size(), loadedIds);
        }

        return loadedWikiPages;
    }
}
