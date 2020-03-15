package com.adambates.wikisearch.search.querying.factories;

import com.adambates.wikisearch.search.factories.DirectoryFactory;
import lombok.AllArgsConstructor;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class IndexReaderFactory {

    private final DirectoryFactory directoryFactory;

    public IndexReader createIndexReader() throws IOException {
        return DirectoryReader.open(directoryFactory.createDirectory());
    }
}
