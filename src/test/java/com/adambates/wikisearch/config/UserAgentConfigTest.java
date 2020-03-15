package com.adambates.wikisearch.config;

import org.junit.jupiter.api.Test;

import static com.adambates.wikisearch.TestObjectFactory.CONTACT_EMAIL;
import static com.adambates.wikisearch.TestObjectFactory.CONTACT_NAME;
import static com.adambates.wikisearch.TestObjectFactory.PROJECT_NAME;
import static com.adambates.wikisearch.TestObjectFactory.PROJECT_VERSION;
import static com.adambates.wikisearch.TestObjectFactory.buildUserAgent;
import static org.assertj.core.api.Assertions.assertThat;

class UserAgentConfigTest {

    private final UserAgentConfig userAgentConfig = new UserAgentConfig(PROJECT_NAME, PROJECT_VERSION, CONTACT_NAME, CONTACT_EMAIL);

    @Test
    void configuresUserAgent() {
        assertThat(userAgentConfig.userAgent())
                .isEqualToComparingFieldByField(buildUserAgent());
    }
}
