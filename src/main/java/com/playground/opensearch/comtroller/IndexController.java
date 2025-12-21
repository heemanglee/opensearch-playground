package com.playground.opensearch.comtroller;

import com.playground.opensearch.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/index")
public class IndexController {

    private final IndexService indexService;

    @PostMapping("/news-articles")
    public void indexingNewArticles(
        @RequestParam("skip") int skip,
        @RequestParam("limit") int limit
    ) {
        indexService.bulkIndexNewsArticles(
            "data/ynat/ynat-v1.1_dev.json",
            skip,
            limit
        );
    }
}
