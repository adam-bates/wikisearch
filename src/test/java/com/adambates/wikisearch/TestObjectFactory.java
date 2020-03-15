package com.adambates.wikisearch;

import com.adambates.wikisearch.config.UserAgentConfig;
import com.adambates.wikisearch.http.models.UserAgent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestObjectFactory {

    public static final String PROJECT_NAME = "projectName";
    public static final String PROJECT_VERSION = "projectVersion";
    public static final String CONTACT_NAME = "contactName";
    public static final String CONTACT_EMAIL = "contactEmail";
    public static final String FRAMEWORK_NAME = UserAgentConfig.FRAMEWORK_NAME;
    public static final String FRAMEWORK_VERSION = UserAgentConfig.FRAMEWORK_VERSION;
    public static final String FRAMEWORK_WEBSITE = UserAgentConfig.FRAMEWORK_WEBSITE;

    public static UserAgent buildUserAgent() {
        return UserAgent.builder()
                .project(UserAgent.Project.builder()
                        .name(PROJECT_NAME)
                        .version(PROJECT_VERSION)
                        .build())
                .contact(UserAgent.Contact.builder()
                        .name(CONTACT_NAME)
                        .email(CONTACT_EMAIL)
                        .build())
                .framework(UserAgent.Framework.builder()
                        .name(FRAMEWORK_NAME)
                        .version(FRAMEWORK_VERSION)
                        .website(FRAMEWORK_WEBSITE)
                        .build())
                .build();
    }
}
