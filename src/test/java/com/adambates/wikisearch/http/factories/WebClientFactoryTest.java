package com.adambates.wikisearch.http.factories;

import com.adambates.wikisearch.http.models.UserAgent;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static com.adambates.wikisearch.TestObjectFactory.buildUserAgent;
import static org.assertj.core.api.Assertions.assertThat;

class WebClientFactoryTest {

    private static final String BASE_URI = "http://localhost";
    private static final UserAgent USER_AGENT = buildUserAgent();

    private final WebClientFactory webClientFactory = new WebClientFactory(USER_AGENT);

    @Test
    void createWebClient() {
        assertThat(webClientFactory.createWebClient(BASE_URI))
                .isInstanceOf(WebClient.class)
                .isNotSameAs(webClientFactory.createWebClient(BASE_URI));
    }
}
