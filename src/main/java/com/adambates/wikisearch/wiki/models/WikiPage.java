package com.adambates.wikisearch.wiki.models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class WikiPage {

    @NonNull
    private final int id;

    @NonNull
    private final String title;
}
