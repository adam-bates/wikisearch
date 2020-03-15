package com.adambates.wikisearch.wiki.gateway;

import com.adambates.wikisearch.http.factories.WebClientFactory;
import com.adambates.wikisearch.http.models.UserAgent;
import com.adambates.wikisearch.http.utils.UserAgentFormatter;
import com.adambates.wikisearch.json.utils.JsonUtils;
import com.adambates.wikisearch.wiki.models.LoadedWikiPage;
import com.adambates.wikisearch.wiki.models.WikiPage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

/**
 * Gateway to Wiki API. This class is responsible for making requests to the wiki api and parsing the response.
 */
@Slf4j
@Component
class WikiGatewayImpl implements WikiGateway {

    private static final String JSON_OBJECT_FIELD_QUERY = "query";
    private static final String JSON_ARRAY_FIELD_RANDOM = "random";
    private static final String JSON_ARRAY_FIELD_PAGES = "pages";

    private final Gson gson;
    private final WebClient webClient;

    WikiGatewayImpl(final Gson gson,
                    final WebClientFactory webClientFactory,
                    @Value("${wiki.api.uri}") final String apiBaseUri) {
        this.gson = gson;
        this.webClient = webClientFactory.createWebClient(apiBaseUri);
    }

    @Override
    public List<WikiPage> queryNRandomWikiPages(final int n) {

        // GET n random wiki pages
        final WebClient.ResponseSpec responseSpec = buildQueryForNRandomPages(n).retrieve();

        // Get JsonArray of wiki pages from API response
        final JsonArray jsonArrayOfPages = getJsonResponseBody(responseSpec)
                .map(jsonObject -> jsonObject.getAsJsonObject(JSON_OBJECT_FIELD_QUERY))
                .map(jsonObject -> jsonObject.getAsJsonArray(JSON_ARRAY_FIELD_RANDOM))
                .orElseGet(JsonArray::new);

        // Convert JsonArray to List<WikiPages> and return
        return JsonUtils.buildListFromJsonArray(jsonArrayOfPages,
                wikiPageJson -> gson.fromJson(wikiPageJson, WikiPage.class));
    }

    @Override
    public LoadedWikiPage queryWikiPageForExtractedContents(final WikiPage wikiPage) {

        // GET content for wikiPage
        final WebClient.ResponseSpec responseSpec = buildQueryForExtractedContentsRequest(wikiPage).retrieve();

        // Get JsonArray of wiki pages from API response
        final JsonArray jsonArrayOfPages = getJsonResponseBody(responseSpec)
                .map(jsonObject -> jsonObject.getAsJsonObject(JSON_OBJECT_FIELD_QUERY))
                .map(jsonObject -> jsonObject.getAsJsonArray(JSON_ARRAY_FIELD_PAGES))
                .orElseGet(JsonArray::new);

        // Convert JsonArray to List<LoadedWikiPage>
        final List<LoadedWikiPage> loadedWikiPages = JsonUtils.buildListFromJsonArray(jsonArrayOfPages, loadedWikiPage -> {
            loadedWikiPage.addProperty("id", loadedWikiPage.get("pageid").getAsInt());
            loadedWikiPage.addProperty("content", loadedWikiPage.get("extract").getAsString());
            return gson.fromJson(loadedWikiPage, LoadedWikiPage.class);
        });

        // Expect exactly one loaded page
        if (loadedWikiPages.size() != 1) {
            throw new IllegalStateException(
                    "Content extraction for wiki page [" + wikiPage + "] must return exactly 1 result," +
                    " but actually returned [" + loadedWikiPages.size() + "] results");
        }

        return loadedWikiPages.get(0);
    }

    private WebClient.RequestHeadersSpec<?> buildQueryForNRandomPages(final int n) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(ActionParam.key(), ActionParam.QUERY.value)
                        .queryParam(ListParam.key(), ListParam.RANDOM.value)
                        .queryParam(NamespaceParam.key(), NamespaceParam.MAIN.value)
                        .queryParam(MaxLagParam.key(), MaxLagParam.ONE.value)
                        .queryParam(FormatVersionParam.key(), FormatVersionParam.TWO.value)
                        .queryParam(FormatParam.key(), FormatParam.JSON.value)
                        .queryParam(ReturnLimitParam.key(), n)
                        .build());
    }

    private WebClient.RequestHeadersSpec<?> buildQueryForExtractedContentsRequest(final WikiPage wikiPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(ActionParam.key(), ActionParam.QUERY.value)
                        .queryParam(PropParam.key(), PropParam.EXTRACTS.value)
                        .queryParam(ExtractLimitParam.key(), ExtractLimitParam.ONE.value)
                        .queryParam(ExplainTextParam.key(), ExplainTextParam.ONE.value)
                        .queryParam(MaxLagParam.key(), MaxLagParam.ONE.value)
                        .queryParam(FormatVersionParam.key(), FormatVersionParam.TWO.value)
                        .queryParam(FormatParam.key(), FormatParam.JSON.value)
                        .queryParam(PageIdsParam.key(), wikiPage.getId())
                        .build());
    }

    private Optional<JsonObject> getJsonResponseBody(final WebClient.ResponseSpec responseSpec) {
        final String response = responseSpec.bodyToMono(String.class).block();
        return Optional.of(gson.fromJson(response, JsonObject.class));
    }


    // ***** QUERY PARAMS FOR THIS CLASS' IMPLEMENTATION ****** //

    @AllArgsConstructor enum ActionParam {
        QUERY("query"),
        ;

        final String value;
        static String key() { return "action"; }
    }

    @AllArgsConstructor enum ListParam {
        RANDOM("random"),
        ;

        final String value;
        static String key() { return "list"; }
    }

    @AllArgsConstructor enum NamespaceParam {
        MAIN(0),
        ;

        final int value;
        static String key() { return "rnnamespace"; }
    }

    @AllArgsConstructor enum ReturnLimitParam {
        ;

        static String key() { return "rnlimit"; }
    }

    @AllArgsConstructor enum ExtractLimitParam {
        ONE(1)
        ;

        final int value;
        static String key() { return "exlimit"; }
    }

    @AllArgsConstructor enum PropParam {
        EXTRACTS("extracts")
        ;

        final String value;
        static String key() { return "prop"; }
    }

    @AllArgsConstructor enum ExplainTextParam {
        ONE(1)
        ;

        final int value;
        static String key() { return "explaintext"; }
    }

    @AllArgsConstructor enum PageIdsParam {
        ;

        static String key() { return "pageids"; }
    }

    @AllArgsConstructor enum MaxLagParam {
        ONE(1)
        ;

        final int value;
        static String key() { return "maxlag"; }
    }

    @AllArgsConstructor enum FormatVersionParam {
        TWO(2)
        ;

        final int value;
        static String key() { return "formatversion"; }
    }

    @AllArgsConstructor enum FormatParam {
        JSON("json")
        ;

        final String value;
        static String key() { return "format"; }
    }
}
