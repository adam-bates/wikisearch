package com.adambates.wikisearch.search.config;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.store.MMapDirectory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemorySearchIndexConfigTest {

    private static final String searchIndexesDir = "searchIndexes";

    private final InMemorySearchIndexConfig inMemorySearchIndexConfig = new InMemorySearchIndexConfig(searchIndexesDir);

    @Test
    void fsDirectory() {
        assertThat(inMemorySearchIndexConfig.fsDirectory())
                .isInstanceOf(MMapDirectory.class)
                .isSameAs(inMemorySearchIndexConfig.fsDirectory());
    }

    @Test
    void analyzer() {
        assertThat(inMemorySearchIndexConfig.analyzer())
                .isInstanceOf(SimpleAnalyzer.class)
                .isSameAs(inMemorySearchIndexConfig.analyzer());
    }
}
