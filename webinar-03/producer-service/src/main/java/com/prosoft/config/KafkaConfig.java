package com.prosoft.config;

import com.prosoft.serializer.PersonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;

public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "localhost:9091, localhost:9092, localhost:9093";

    private KafkaConfig() {
    }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PersonSerializer.class.getName());

        return properties;
    }


}
