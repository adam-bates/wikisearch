package com.adambates.wikisearch.search.config;

import lombok.SneakyThrows;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
class InMemorySearchIndexConfig {

    private final FSDirectory fsDirectory;
    private final Analyzer analyzer;

    InMemorySearchIndexConfig(@Value("${search.indexes.dir}") final String searchIndexesDir) {
        this.fsDirectory = buildInMemoryFSDirectory(searchIndexesDir);
        this.analyzer = new SimpleAnalyzer();
    }

    @Bean
    FSDirectory fsDirectory() {
        return fsDirectory;
    }

    @Bean
    Analyzer analyzer() {
        return analyzer;
    }

    @SneakyThrows(IOException.class)
    private static FSDirectory buildInMemoryFSDirectory(final String searchIndexesDir) {
        return MMapDirectory.open(Paths.get(searchIndexesDir));
    }
}
