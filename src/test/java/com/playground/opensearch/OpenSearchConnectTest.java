package com.playground.opensearch;

import java.io.IOException;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.InfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenSearchConnectTest {

    @Autowired
    OpenSearchClient openSearchClient;

    @Test
    void testConnection() throws IOException {
        InfoResponse info = openSearchClient.info();
        System.out.println("Connected to OpenSearch! Version: " + info.version().number());
    }
}
