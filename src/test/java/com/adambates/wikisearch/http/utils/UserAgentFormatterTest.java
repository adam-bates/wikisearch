package com.adambates.wikisearch.http.utils;

import com.adambates.wikisearch.http.models.UserAgent;
import org.junit.jupiter.api.Test;

import static com.adambates.wikisearch.TestObjectFactory.CONTACT_EMAIL;
import static com.adambates.wikisearch.TestObjectFactory.CONTACT_NAME;
import static com.adambates.wikisearch.TestObjectFactory.FRAMEWORK_NAME;
import static com.adambates.wikisearch.TestObjectFactory.FRAMEWORK_VERSION;
import static com.adambates.wikisearch.TestObjectFactory.FRAMEWORK_WEBSITE;
import static com.adambates.wikisearch.TestObjectFactory.PROJECT_NAME;
import static com.adambates.wikisearch.TestObjectFactory.PROJECT_VERSION;
import static com.adambates.wikisearch.TestObjectFactory.buildUserAgent;
import static org.assertj.core.api.Assertions.assertThat;

class UserAgentFormatterTest {

    private static final UserAgent USER_AGENT = buildUserAgent();

    @Test
    void formatUserAgentHeaderValue() {
        assertThat(UserAgentFormatter.formatUserAgentHeaderValue(USER_AGENT))
                .isEqualTo(String.format("%s/%s (%s; %s) %s/%s (%s)",
                        PROJECT_NAME, PROJECT_VERSION,
                        CONTACT_NAME, CONTACT_EMAIL,
                        FRAMEWORK_NAME, FRAMEWORK_VERSION, FRAMEWORK_WEBSITE));
    }
}
