package com.playground.opensearch.dto;

import java.util.List;

public record BulkResult(
    String indexName,
    int requestedCount,
    int successCount,
    int failedCount,
    long elapsedTimeMs,
    List<String> failedIds
) {

    public boolean hasErrors() {
        return failedCount > 0;
    }
}
