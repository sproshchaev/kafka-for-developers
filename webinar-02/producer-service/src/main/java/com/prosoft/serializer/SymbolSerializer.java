package com.prosoft.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prosoft.domain.Symbol;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class SymbolSerializer implements Serializer<Symbol> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Symbol data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Person to JSON", e);
        }
    }

    @Override
    public void close() {
    }
}
