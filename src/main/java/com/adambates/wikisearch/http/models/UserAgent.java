package com.adambates.wikisearch.http.models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UserAgent {

    private final Contact contact;
    private final Project project;
    private final Framework framework;

    private UserAgent(@NonNull final Contact contact,
                      @NonNull final Project project,
                      @NonNull final Framework framework) {
        this.contact = contact;
        this.project = project;
        this.framework = framework;
    }

    @Data
    @Builder
    public static class Contact {
        private final String name;
        private final String email;
    }

    @Data
    @Builder
    public static class Project {
        private final String name;
        private final String version;
    }

    @Data
    @Builder
    public static class Framework {
        private final String name;
        private final String version;
        private final String website;
    }
}
