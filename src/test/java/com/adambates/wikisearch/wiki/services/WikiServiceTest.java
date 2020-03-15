package com.adambates.wikisearch.wiki.services;

import com.adambates.wikisearch.wiki.gateway.WikiGateway;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import com.adambates.wikisearch.wiki.models.WikiPage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WikiServiceTest {

    private static final int PAGES_TO_LOAD = 3;

    private final WikiGateway wikiGateway = mock(WikiGateway.class);
    private final WikiService wikiService = new WikiServiceImpl(wikiGateway);

    @Test
    void loadNRandomWikiPages() {
        final WikiPage
                wikiPage1 = buildWikiPage1(),
                wikiPage2 = buildWikiPage2(),
                wikiPage3 = buildWikiPage3();

        final LoadedWikiPage
                loadedWikiPage1 = buildLoadedWikiPage1(),
                loadedWikiPage2 = buildLoadedWikiPage2(),
                loadedWikiPage3 = buildLoadedWikiPage3();

        when(wikiGateway.queryNRandomWikiPages(PAGES_TO_LOAD))
                .thenReturn(Arrays.asList(wikiPage1, wikiPage2, wikiPage3));

        when(wikiGateway.queryWikiPageForExtractedContents(wikiPage1)).thenReturn(loadedWikiPage1);
        when(wikiGateway.queryWikiPageForExtractedContents(wikiPage2)).thenReturn(loadedWikiPage2);
        when(wikiGateway.queryWikiPageForExtractedContents(wikiPage3)).thenReturn(loadedWikiPage3);

        final List<LoadedWikiPage> loadedWikiPages = wikiService.loadNRandomWikiPages(PAGES_TO_LOAD);

        assertThat(loadedWikiPages).containsExactly(loadedWikiPage1, loadedWikiPage2, loadedWikiPage3);
    }

    private static WikiPage buildWikiPage1() {
        return WikiPage.builder()
                .id(1)
                .title("wikiPage1")
                .build();
    }

    private static WikiPage buildWikiPage2() {
        return WikiPage.builder()
                .id(2)
                .title("wikiPage2")
                .build();
    }

    private static WikiPage buildWikiPage3() {
        return WikiPage.builder()
                .id(3)
                .title("wikiPage3")
                .build();
    }

    private static LoadedWikiPage buildLoadedWikiPage1() {
        final WikiPage wikiPage1 = buildWikiPage1();

        return LoadedWikiPage.builder()
                .id(wikiPage1.getId())
                .title(wikiPage1.getTitle())
                .content("loadedContent1")
                .build();
    }

    private static LoadedWikiPage buildLoadedWikiPage2() {
        final WikiPage wikiPage2 = buildWikiPage2();

        return LoadedWikiPage.builder()
                .id(wikiPage2.getId())
                .title(wikiPage2.getTitle())
                .content("loadedContent2")
                .build();
    }

    private static LoadedWikiPage buildLoadedWikiPage3() {
        final WikiPage wikiPage3 = buildWikiPage3();

        return LoadedWikiPage.builder()
                .id(wikiPage3.getId())
                .title(wikiPage3.getTitle())
                .content("loadedContent3")
                .build();
    }
}
