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
        indexService.indexNewsArticle("data/ynat/ynat-v1.1_dev.json", 0, 100);
    }

}