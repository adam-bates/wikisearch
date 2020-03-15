package com.adambates.wikisearch.search.indexing;

import com.adambates.wikisearch.search.DocumentFieldName;
import com.adambates.wikisearch.search.indexing.factories.IndexWriterFactory;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
class IndexWriterServiceImpl implements IndexWriterService {

    private final IndexWriterFactory indexWriterFactory;
    private final Analyzer analyzer;

    @Override
    public void writeLoadedWikiPagesToIndex(final List<LoadedWikiPage> loadedWikiPages) {
        try (final IndexWriter writer = indexWriterFactory.createIndexWriter()) {
            writeLoadedWikiPagesToIndex(writer, loadedWikiPages);
        } catch (final IOException e) {
            log.error("Unable to write documents to index: ", e);
            throw new RuntimeException(e);
        }
    }

    private void writeLoadedWikiPagesToIndex(final IndexWriter writer,
                                             final List<LoadedWikiPage> loadedWikiPages) throws IOException {
        for (final LoadedWikiPage loadedWikiPage : loadedWikiPages) {
            writer.addDocument(buildDocument(loadedWikiPage));
        }
    }

    private static Document buildDocument(final LoadedWikiPage loadedWikiPage) {
        final Document document = new Document();
        document.add(new TextField(DocumentFieldName.PAGE_ID.getValue(), String.valueOf(loadedWikiPage.getId()), Field.Store.YES));
        document.add(new TextField(DocumentFieldName.NAME.getValue(), loadedWikiPage.getTitle(), Field.Store.YES));
        document.add(new TextField(DocumentFieldName.CONTENT.getValue(), loadedWikiPage.getContent(), Field.Store.YES));
        return document;
    }
}
