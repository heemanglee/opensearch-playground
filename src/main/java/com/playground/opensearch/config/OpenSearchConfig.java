package com.playground.opensearch.config;

import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Bean
    public OpenSearchClient openSearchClient() {
        final HttpHost httpHost = new HttpHost("http", "localhost", 9200);

        OpenSearchTransport transport = ApacheHttpClient5TransportBuilder
            .builder(new HttpHost[]{httpHost})
            .setMapper(new JacksonJsonpMapper())
            .build();

        return new OpenSearchClient(transport);
    }
}
