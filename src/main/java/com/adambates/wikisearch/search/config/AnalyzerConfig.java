package com.adambates.wikisearch.search.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AnalyzerConfig {

    private final Analyzer analyzer = new StandardAnalyzer();

    @Bean
    Analyzer analyzer() {
        return analyzer;
    }
}
