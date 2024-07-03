package com.prosoft.config;

import com.prosoft.serializer.PersonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;

public class KafkaConfig02 {

    public static final String TOPIC = "topic1";

    private static final String BOOTSTRAP_SERVERS = "localhost:9094";

    private KafkaConfig02() { }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        //
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        // TRANSACTIONAL_ID_CONFIG   // IllegalStateException: Transactional method invoked on a non-transactional producer.
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");

        // TRANSACTION_TIMEOUT_CONFIG
        properties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 600000);

        // .ISOLATION_LEVEL_CONFIG, "read_committed" - параметр для конфигурации кансамера!!!

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PersonSerializer.class.getName());

        return properties;
    }

    }
