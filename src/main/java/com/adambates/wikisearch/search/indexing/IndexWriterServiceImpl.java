package com.adambates.wikisearch.search.indexing;

import com.adambates.wikisearch.search.TextFieldName;
import com.adambates.wikisearch.search.indexing.factories.IndexWriterFactory;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
class IndexWriterServiceImpl implements IndexWriterService {

    private final IndexWriterFactory indexWriterFactory;

    @Override
    public void writeLoadedWikiPagesToIndex(final List<LoadedWikiPage> loadedWikiPages) {
        final List<Document> documents = loadedWikiPages.stream()
                .map(IndexWriterServiceImpl::buildDocument)
                .collect(Collectors.toList());

        writeDocumentsToIndex(documents);
    }

    private void writeDocumentsToIndex(final List<Document> documents) {
        try (final IndexWriter writer = indexWriterFactory.createIndexWriter()) {
            writer.addDocuments(documents);
        } catch (final IOException e) {
            log.error("Unable to write documents to index: ", e);
            throw new RuntimeException(e);
        }
    }

    private static Document buildDocument(final LoadedWikiPage loadedWikiPage) {
        final Document document = new Document();
        document.add(new TextField(TextFieldName.NAME.getValue(), loadedWikiPage.getTitle(), Field.Store.YES));
        document.add(new TextField(TextFieldName.CONTENT.getValue(), loadedWikiPage.getContent(), Field.Store.YES));
        return document;
    }
}
