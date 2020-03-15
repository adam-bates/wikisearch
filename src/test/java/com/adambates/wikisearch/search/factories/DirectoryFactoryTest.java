package com.adambates.wikisearch.search.factories;

import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DirectoryFactoryTest {

    private static final String SEARCH_INDEXES_DIR = "searchIndexes";

    private final DirectoryFactory directoryFactory = new DirectoryFactory(SEARCH_INDEXES_DIR);

    @Test
    void createDirectory() {
        assertThat(directoryFactory.createDirectory())
                .isInstanceOf(FSDirectory.class)
                .isNotSameAs(directoryFactory.createDirectory());
    }
}
