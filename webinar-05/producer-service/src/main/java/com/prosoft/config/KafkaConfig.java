package com.prosoft.config;

import com.prosoft.serializer.PersonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaConfig {

    public static final String TOPIC = "w06-topic-in";

    private static final String BOOTSTRAP_SERVERS = "localhost:9093";

    public static Properties getProducerConfig() {

        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        // ENABLE_IDEMPOTENCE_CONFIG = "enable.idempotence";  enable.idempotence=true
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        // (1) ACKS_CONFIG = "acks"; "all" // ConfigException: Must set acks to all in order to use the idempotent producer. Otherwise we cannot guarantee idempotence.
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        // (2) RETRIES_CONFIG = "retries";   > 0; // ConfigException: Must set retries to non-zero when using the idempotent producer.
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);

        // (3) MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION = "max.in.flight.requests.per.connection"; <=5  // ConfigException: Must set max.in.flight.requests.per.connection to at most 5 to use the idempotent producer
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PersonSerializer.class.getName());

        return properties;
    }

}
