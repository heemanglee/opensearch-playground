package com.playground.opensearch.index;

import java.util.function.Function;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.opensearch.client.opensearch.indices.IndexSettings.Builder;
import org.opensearch.client.util.ObjectBuilder;

public interface IndexDefinition {

    String getIndexName();

    Function<Builder, ObjectBuilder<IndexSettings>> getSettings();

    Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> getMappings();
}
