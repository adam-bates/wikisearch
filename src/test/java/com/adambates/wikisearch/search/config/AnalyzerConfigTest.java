package com.adambates.wikisearch.search.config;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyzerConfigTest {

    private final AnalyzerConfig analyzerConfig = new AnalyzerConfig();

    @Test
    void analyzer() {
        assertThat(analyzerConfig.analyzer())
                .isInstanceOf(StandardAnalyzer.class)
                .isSameAs(analyzerConfig.analyzer());
    }
}
