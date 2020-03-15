package com.adambates.wikisearch.search.indexing;

import com.adambates.wikisearch.wiki.models.LoadedWikiPage;

import java.util.List;

public interface IndexWriterService {

    void writeLoadedWikiPagesToIndex(final List<LoadedWikiPage> loadedWikiPages);
}
