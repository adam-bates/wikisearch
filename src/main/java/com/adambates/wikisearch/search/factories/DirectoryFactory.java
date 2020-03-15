package com.adambates.wikisearch.search.factories;

import lombok.SneakyThrows;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class DirectoryFactory {

    private final String searchIndexesDir;

    DirectoryFactory(@Value("${search.indexes.dir}") final String searchIndexesDir) {
        this.searchIndexesDir = searchIndexesDir;
    }

    @SneakyThrows
    public FSDirectory createDirectory() {
        return MMapDirectory.open(Paths.get(searchIndexesDir));
    }
}
