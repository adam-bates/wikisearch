package com.adambates.wikisearch.wiki.gateway;

import com.adambates.wikisearch.http.factories.WebClientFactory;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import com.adambates.wikisearch.wiki.models.WikiPage;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WikiGatewayTest {

    private static final String BASE_API_URI = "http://baseuri";

    private static final int NUMBER_OF_RANDOM_PAGES = 3;
    private static final String EXAMPLE_N_RANDOM_JSON = "{" +
            "\"batchcomplete\":\"\"," +
            "\"continue\":{" +
            "\"rncontinue\":\"0.095778699359|0.095779691053|23603332|0\"," +
            "\"continue\":\"-||\"" +
            "},\"query\":{" +
            "\"random\":[{\"id\":54453383,\"ns\":0,\"title\":\"Brenda Morehead\"},{\"id\":36031682,\"ns\":0,\"title\":\"Roger Sommer\"},{\"id\":60386179,\"ns\":0,\"title\":\"New Buffalo Times\"}]" +
            "}}";

    private static final WikiPage WIKI_PAGE = WikiPage.builder()
            .id(47391)
            .title("307")
            .build();
    private static final String EXAMPLE_EXTRACTED_JSON = "{" +
            "\"batchcomplete\":true," +
            "\"query\":{" +
            "\"pages\":[{" +
            "\"pageid\":47391," +
            "\"ns\":0," +
            "\"title\":\"307\"," +
            "\"extract\":\"Year 307 (CCCVII) was a common year starting on Wednesday (link will display the full calendar) of the Julian calendar. At the time, it was known as the Year of the Consulship of Valerius and Constantius (or, less frequently, year 1060 Ab urbe condita).\"" +
            "}]}}";

    private final Gson gson = new Gson();
    private final WebClientFactory webClientFactory = mock(WebClientFactory.class);

    private final WebClient webClient = mock(WebClient.class);
    private final WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);

    private WikiGateway wikiGateway;

    @BeforeEach
    void setUp() {
        when(webClientFactory.createWebClient(anyString())).thenReturn(webClient);
        wikiGateway = new WikiGatewayImpl(gson, webClientFactory, BASE_API_URI);
    }

    @AfterEach
    void takeDown() {
        verify(webClientFactory).createWebClient(BASE_API_URI);
    }

    @Test
    void queryNRandomWikiPages() {
        prepareWebClientForGet(EXAMPLE_N_RANDOM_JSON);

        assertThat(wikiGateway.queryNRandomWikiPages(NUMBER_OF_RANDOM_PAGES))
                .hasSize(NUMBER_OF_RANDOM_PAGES);
    }

    @Test
    void queryWikiPageForExtractedContents() {
        prepareWebClientForGet(EXAMPLE_EXTRACTED_JSON);

        final LoadedWikiPage loadedWikiPage = wikiGateway.queryWikiPageForExtractedContents(WIKI_PAGE);

        assertThat(loadedWikiPage.getId()).isEqualTo(WIKI_PAGE.getId());
        assertThat(loadedWikiPage.getTitle()).isEqualTo(WIKI_PAGE.getTitle());

        assertThat(loadedWikiPage.getContent()).isNotBlank();
        assertThat(EXAMPLE_EXTRACTED_JSON).contains(loadedWikiPage.getContent());
    }

    @SuppressWarnings("unchecked")
    private void prepareWebClientForGet(final String jsonBody) {
        final Mono<String> mono = mock(Mono.class);
        when(mono.block()).thenReturn(jsonBody);

        final WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        when(responseSpec.bodyToMono(String.class)).thenReturn(mono);

        final WebClient.RequestHeadersSpec<?> requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
    }
}
