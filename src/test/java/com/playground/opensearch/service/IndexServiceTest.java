package com.playground.opensearch.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IndexServiceTest {

    @Autowired
    IndexService indexService;

    @Test
    void index() throws Exception {
        indexService.indexNewsArticle("data/ynat/ynat-v1.1_dev.json", 100, 900);
    }

    @Test
    void calculateSingleIndexingPerformance() throws Exception {
        long startTime = System.currentTimeMillis();

        int count = 10000;
        indexService.indexNewsArticle("data/ynat/ynat-v1.1_dev.json", 0, count);

        long endTime = System.currentTimeMillis();

        System.out.println(String.format("단건 인덱싱 %d건 소요 시간: %dms", count, (endTime - startTime)));
    }

    @Test
    void calculateBulkIndexingPerformance() {
        long startTime = System.currentTimeMillis();

        int count = 10_000;
        indexService.bulkIndexNewsArticles("data/ynat/ynat-v1.1_train.json", 0, count);
    }

}