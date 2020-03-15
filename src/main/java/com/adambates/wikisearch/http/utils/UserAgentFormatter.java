package com.adambates.wikisearch.http.utils;

import com.adambates.wikisearch.http.models.UserAgent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserAgentFormatter {

    /**
     * Returns a string value of a User-Agent header using the {@link UserAgent} parameter.
     *
     * Format of return value:
     *   "project.name/project.version (contact.name; contact.email) framework.name/framework.version (framework.website)"
     *
     * @param userAgent UserAgent
     * @return String value of User-Agent header
     */
    public static String formatUserAgentHeaderValue(final UserAgent userAgent) {

        final StringBuilder stringBuilder = new StringBuilder();
        appendProjectValue(stringBuilder, userAgent.getProject());

        stringBuilder.append(" ");
        appendContactValue(stringBuilder, userAgent.getContact());

        stringBuilder.append(" ");
        appendFrameworkValue(stringBuilder, userAgent.getFramework());

        return stringBuilder.toString();
    }

    private static void appendProjectValue(final StringBuilder stringBuilder, final UserAgent.Project project) {
        stringBuilder
                .append(project.getName())
                .append("/")
                .append(project.getVersion());
    }

    private static void appendContactValue(final StringBuilder stringBuilder, final UserAgent.Contact contact) {
        stringBuilder
                .append("(")
                .append(contact.getName())
                .append("; ")
                .append(contact.getEmail())
                .append(")");
    }

    private static void appendFrameworkValue(final StringBuilder stringBuilder, final UserAgent.Framework framework) {
        stringBuilder
                .append(framework.getName())
                .append("/")
                .append(framework.getVersion())
                .append(" (")
                .append(framework.getWebsite())
                .append(")");
    }
}
