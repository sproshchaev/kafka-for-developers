package com.prosoft.config;

import com.prosoft.deserializer.PersonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.util.Properties;

public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "localhost:9091, localhost:9092, localhost:9093";

    private KafkaConfig() {
    }

    public static Properties getConsumerConfig() {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS); // !

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group"); // !

        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);

        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);

        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);

        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1048576);

        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName()); // !

        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PersonDeserializer.class.getName()); // !

        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }

}
