package com.adambates.wikisearch.wiki.gateway;

import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import com.adambates.wikisearch.wiki.models.WikiPage;

import java.util.List;

public interface WikiGateway {

    List<WikiPage> queryNRandomWikiPages(int n);
    LoadedWikiPage queryWikiPageForExtractedContents(WikiPage wikiPage);
}
