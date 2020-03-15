package com.adambates.wikisearch.http.config;

import com.adambates.wikisearch.http.models.UserAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringVersion;

@Configuration
public class UserAgentConfig {

    public static final String FRAMEWORK_NAME = "Spring";
    public static final String FRAMEWORK_VERSION = SpringVersion.getVersion();
    public static final String FRAMEWORK_WEBSITE = "https://spring.io/";

    private final UserAgent userAgent;

    UserAgentConfig(@Value("${http.header.user-agent.project.name}") final String projectName,
                    @Value("${http.header.user-agent.project.version}") final String projectVersion,
                    @Value("${http.header.user-agent.contact.name}") final String contactName,
                    @Value("${http.header.user-agent.contact.email}") final String contactEmail) {
        this.userAgent = UserAgent.builder()
                .project(UserAgent.Project.builder()
                        .name(projectName)
                        .version(projectVersion)
                        .build())
                .contact(UserAgent.Contact.builder()
                        .name(contactName)
                        .email(contactEmail)
                        .build())
                .framework(UserAgent.Framework.builder()
                        .name(FRAMEWORK_NAME)
                        .version(FRAMEWORK_VERSION)
                        .website(FRAMEWORK_WEBSITE)
                        .build())
                .build();
    }

    @Bean
    public UserAgent userAgent() {
        return userAgent;
    }
}
