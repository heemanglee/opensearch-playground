package com.playground.opensearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewsArticle(
    String guid,
    String title,
    @JsonProperty("predefined_news_category") String category,
    String label,
    String date,
    String url
) {
}
