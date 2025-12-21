package com.playground.opensearch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.opensearch.dto.BulkResult;
import com.playground.opensearch.dto.NewsArticle;
import com.playground.opensearch.dto.NewsArticleDocument;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
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

    public void bulkIndexNewsArticles(String filePath, int skip, int limit) {
        final int BATCH_SIZE = 1000;
        final String indexName = "news_articles";

        try {
            List<NewsArticle> articles = objectMapper.readValue(
                new File(filePath),
                new TypeReference<List<NewsArticle>>() {
                }
            );

            List<BulkOperation> operations = new ArrayList<>();
            int count = 0;

            for (int i = skip; i < articles.size() && count < limit; i++) {
                NewsArticle article = articles.get(i);
                NewsArticleDocument document = NewsArticleDocument.from(article);

                operations.add(BulkOperation.of(op ->
                        op.index(idx ->
                            idx.index(indexName)
                                .id(article.guid())
                                .document(document)
                        )
                    )
                );

                count++;

                // 배치 크기에 도달하면 인덱싱 요청
                if (operations.size() >= BATCH_SIZE) {
                    executeBulk(indexName, operations);
                    operations.clear();
                }
            }

            // 남은 문서 인덱싱
            if (!operations.isEmpty()) {
                executeBulk(indexName, operations);
            }

        } catch (Exception ex) {
            log.error("Bulk indexing failed. [index={}, skip={}, limit={}]",
                indexName, skip, limit, ex);
            throw new RuntimeException("Bulk indexing failed", ex);
        }
    }

    private BulkResult executeBulk(String indexName, List<BulkOperation> operations) throws Exception {
        long startTime = System.currentTimeMillis();

        BulkRequest bulkRequest = BulkRequest.of(req -> req.operations(operations));
        BulkResponse response = client.bulk(bulkRequest);

        long elapsedTime = System.currentTimeMillis() - startTime;

        List<String> failedIds = response.items().stream()
            .filter(item -> item.error() != null)
            .map(item -> item.id())
            .toList();

        int successCount = operations.size() - failedIds.size();

        if (response.errors()) {
            log.error("Bulk indexing failure. [index={}, success={}, failed={}, elapsed={}ms, failedIds={}]",
                indexName, successCount, failedIds.size(), elapsedTime, failedIds
            );
        } else {
            log.info("Bulk indexing success. [index={}, success={}, elapsed={}ms]",
                indexName, successCount, elapsedTime
            );
        }

        return new BulkResult(
            indexName,
            operations.size(),
            successCount,
            failedIds.size(),
            elapsedTime,
            failedIds
        );
    }
}
