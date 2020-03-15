package com.adambates.wikisearch.search.querying.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TermResults {
    private final long totalTermsIndexed;
    private final List<TermResult> terms;
}
