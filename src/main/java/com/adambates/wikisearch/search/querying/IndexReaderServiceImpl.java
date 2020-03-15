package com.adambates.wikisearch.search.querying;

import com.adambates.wikisearch.search.DocumentFieldName;
import com.adambates.wikisearch.search.querying.factories.IndexReaderFactory;
import com.adambates.wikisearch.search.querying.factories.IndexSearcherFactory;
import com.adambates.wikisearch.search.querying.models.SearchResult;
import com.adambates.wikisearch.search.querying.models.SearchResults;
import com.adambates.wikisearch.search.querying.models.TermResult;
import com.adambates.wikisearch.search.querying.models.TermResults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
class IndexReaderServiceImpl implements IndexReaderService {

    private final IndexReaderFactory indexReaderFactory;
    private final IndexSearcherFactory indexSearcherFactory;
    private final String wikiPageBaseLink;

    IndexReaderServiceImpl(final IndexReaderFactory indexReaderFactory,
                           final IndexSearcherFactory indexSearcherFactory,
                           @Value("${wiki.page.uri}") String wikiPageBaseLink) {
        this.indexReaderFactory = indexReaderFactory;
        this.indexSearcherFactory = indexSearcherFactory;
        this.wikiPageBaseLink = wikiPageBaseLink;
    }

    @Override
    public SearchResults getNSearchResultsForQuery(final Query query, final int n) {
        try (final IndexReader reader = indexReaderFactory.createIndexReader()) {
            return getSearchResultsForQuery(reader, query, n);
        } catch (final IOException e) {
            log.error("Unable to read index for query: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public TermResults getNTermResults(final int n) {
        try (final IndexReader reader = indexReaderFactory.createIndexReader()) {
            return getTermResults(reader, n);
        } catch (final IOException e) {
            log.error("Unable to read index terms: ", e);
            throw new RuntimeException(e);
        }
    }

    private SearchResults getSearchResultsForQuery(final IndexReader indexReader, final Query query, final int n) throws IOException {
        final long totalTermsIndexed = indexReader.getSumTotalTermFreq(DocumentFieldName.CONTENT.getValue());

        final IndexSearcher indexSearcher = indexSearcherFactory.createIndexSearcher(indexReader);
        final TopDocs topDocs = indexSearcher.search(query, n);
        final List<SearchResult> searchResults = Arrays.stream(topDocs.scoreDocs)
                .map(scoreDoc -> {
                    final Document document = getDocument(indexSearcher, scoreDoc);
                    if (document == null) {
                        return null;
                    }

                    final int wikiPageId = getWikiPageId(document);
                    if (wikiPageId < 0) {
                        return null;
                    }

                    final String title = document.getField(DocumentFieldName.NAME.getValue()).stringValue();
                    final String content = document.getField(DocumentFieldName.CONTENT.getValue()).stringValue();

                    return SearchResult.builder()
                            .id(scoreDoc.doc)
                            .score(scoreDoc.score)
                            .wikiPageId(wikiPageId)
                            .wikiPageLink(buildWikiPageUri(title))
                            .title(title)
                            .content(content)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return SearchResults.builder()
                .totalTermsIndexed(totalTermsIndexed)
                .pages(searchResults)
                .build();
    }

    private TermResults getTermResults(final IndexReader indexReader, final int n) throws IOException {
        final long totalTermsIndexed = indexReader.getSumTotalTermFreq(DocumentFieldName.CONTENT.getValue());
        final List<TermResult> termResults = getContentTermResults(indexReader, n);

        return TermResults.builder()
                .totalTermsIndexed(totalTermsIndexed)
                .terms(termResults)
                .build();
    }

    /**
     * Builds wiki page uri. For example:
     *
     * If wikiPageBaseLink is "https://en.wikipedia.org/wiki/"
     * And title is "Some Example Page"
     * Then this function returns "https://en.wikipedia.org/wiki/Some_Example_Page"
     *
     * @param title String
     * @return String
     */
    private String buildWikiPageUri(final String title) {
        return wikiPageBaseLink + StringUtils.replace(title, " ", "_");
    }

    private static Document getDocument(final IndexSearcher indexSearcher, final ScoreDoc scoreDoc) {
        try {
            return indexSearcher.doc(scoreDoc.doc);
        } catch (final IOException e) {
            log.error("Unable to get doc: ", e);
            return null;
        }
    }

    private static int getWikiPageId(final Document document) {
        try {
            return Integer.parseInt(document.getField(DocumentFieldName.PAGE_ID.getValue()).stringValue());
        } catch (final NumberFormatException e) {
            log.error("Unable to parse wikiPageId as an integer: ", e);
            return -1;
        }
    }

    private static List<TermResult> getContentTermResults(final IndexReader indexReader, final int n) {
        try {
            final TermStats[] stats = HighFreqTerms.getHighFreqTerms(
                    indexReader, n, DocumentFieldName.CONTENT.getValue(), new HighFreqTerms.DocFreqComparator());

            return Arrays.stream(stats)
                    .map(termStats -> TermResult.builder()
                            .totalFrequency(termStats.totalTermFreq)
                            .wikiPageFrequency(termStats.docFreq)
                            .term(termStats.termtext.utf8ToString())
                            .build())
                    .collect(Collectors.toList());
        } catch (final Exception e) {
            log.error("Unable to get high-frequency terms: ", e);
            return Collections.emptyList();
        }
    }
}
