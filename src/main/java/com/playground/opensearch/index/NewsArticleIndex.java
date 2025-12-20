package com.playground.opensearch.index;

import java.util.function.Function;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.opensearch.client.opensearch.indices.IndexSettings.Builder;
import org.opensearch.client.util.ObjectBuilder;
import org.springframework.stereotype.Component;


@Component
public class NewsArticleIndex implements IndexDefinition {
    @Override
    public String getIndexName() {
        return "news_articles";
    }

    @Override
    public Function<Builder, ObjectBuilder<IndexSettings>> getSettings() {
        return settings -> settings
            .analysis(analysis -> analysis
                .analyzer("korean_analyzer", analyzer -> analyzer
                    .custom(custom -> custom
                        .tokenizer("nori_tokenizer")
                        .filter("lowercase")
                    )
                )
            );
    }

    @Override
    public Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> getMappings() {
        return mappings -> mappings
            .properties("title", p -> p
                .text(t -> t.analyzer("korean_analyzer"))
            )
            .properties("label", p -> p
                .keyword(k -> k)
            )
            .properties("date", p -> p
                .text(t -> t)
            )
            .properties("url", p -> p
                .keyword(k -> k)
            );
    }
}

