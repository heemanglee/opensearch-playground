package com.playground.opensearch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.opensearch.dto.NewsArticle;
import com.playground.opensearch.dto.NewsArticleDocument;
import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexService {

    private final OpenSearchClient client;
    private final ObjectMapper objectMapper;

    public void indexNewsArticle(String filePath, int skip, int limit) throws Exception {
        final String indexName = "news_articles";

        // JSON 파일 파싱
        List<NewsArticle> articles = objectMapper.readValue(
            new File(filePath),
            new TypeReference<List<NewsArticle>>() {
            }
        );

        // 인덱싱
        int count = 0;
        for (int i = skip; i < articles.size() && count < limit; i++) {
            NewsArticle article = articles.get(i);
            NewsArticleDocument document = NewsArticleDocument.from(article);

            this.client.index(IndexRequest.of(req ->
                req.index(indexName)
                    .id(article.guid())
                    .document(document)
            ));

            count++;
        }

        log.info("Indexing completed. [index={}, skip={}, limit={}, indexed={}]", indexName, skip, limit, count);
    }
}
