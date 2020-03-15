package com.adambates.wikisearch.wiki.services;

import com.adambates.wikisearch.wiki.models.LoadedWikiPage;

import java.util.List;

public interface WikiService {

    List<LoadedWikiPage> loadNRandomWikiPages(final int n);
}
