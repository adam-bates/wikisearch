package com.adambates.wikisearch.search.querying.factories;

import lombok.AllArgsConstructor;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class IndexReaderFactory {

    private final FSDirectory fsDirectory;

    public IndexReader createIndexReader() throws IOException {
        return DirectoryReader.open(fsDirectory);
    }
}
