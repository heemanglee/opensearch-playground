package com.playground.opensearch.dto;

public record NewsArticleDocument(
    String guid,
    String title,
    String category,
    String label,
    String date,
    String url
) {
    public static NewsArticleDocument from(NewsArticle article) {
        return new NewsArticleDocument(
            article.guid(),
            article.title(),
            article.category(),
            article.label(),
            article.date(),
            article.url()
        );
    }
}
