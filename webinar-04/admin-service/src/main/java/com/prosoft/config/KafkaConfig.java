package com.prosoft.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "localhost:9091, localhost:9092, localhost:9093";

    private KafkaConfig() {
    }

    public static Properties getAdminConfig() {

        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        return properties;
    }


}
