package com.playground.opensearch.index;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IndexInitializer {

    private final OpenSearchClient client;
    private final List<IndexDefinition> indexDefinitions;

    @PostConstruct
    public void initializeAll() {
        for (IndexDefinition index : indexDefinitions) {
            createIndexIfNotExists(index);
        }
    }

    private void createIndexIfNotExists(IndexDefinition index) {
        try {
            String indexName = index.getIndexName();

            boolean exists = this.client.indices().exists(req -> req.index(indexName)).value();

            if (exists) {
                log.info("인덱스기 이미 존재합니다: {}", indexName);
                return;
            }

            this.client.indices()
                    .create(req -> req.index(indexName).settings(index.getSettings()).mappings(index.getMappings()));

            log.info("인덱스 생성 완료: {}", indexName);
        } catch (Exception ex) {
            log.info("인덱스 생성 실패: {}", index.getIndexName(), ex);
            throw new RuntimeException("인덱스 생성 실패", ex);
        }
    }

}
