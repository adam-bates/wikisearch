package com.adambates.wikisearch.search.indexing.factories;

import com.adambates.wikisearch.search.factories.DirectoryFactory;
import lombok.AllArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class IndexWriterFactory {

    private final DirectoryFactory directoryFactory;
    private final Analyzer analyzer;

    public IndexWriter createIndexWriter() throws IOException {
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        return new IndexWriter(directoryFactory.createDirectory(), indexWriterConfig);
    }
}
