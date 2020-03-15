package com.adambates.wikisearch.http.factories;

import com.adambates.wikisearch.http.models.UserAgent;
import com.adambates.wikisearch.http.utils.UserAgentFormatter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@AllArgsConstructor
public class WebClientFactory {

    private final UserAgent userAgent;

    public WebClient createWebClient(final String baseUri) {
        return WebClient.builder()
                .baseUrl(baseUri)
                .defaultHeader(HttpHeaders.USER_AGENT, UserAgentFormatter.formatUserAgentHeaderValue(userAgent))
                .build();
    }
}
