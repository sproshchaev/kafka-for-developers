package com.prosoft;

import com.prosoft.config.KafkaConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Webinar-07: Kafka consumer-service
 */
public class KafkaConsumer02App {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer02App.class);
    private static final Duration TEN_MILLISECONDS_INTERVAL = Duration.ofMillis(10);

    public static void main(String[] args) {
        // KafkaConsumer with GenericRecord as value
        KafkaConsumer<Long, GenericRecord> consumer = new KafkaConsumer<>(KafkaConfig.getConsumerConfig());
        try (consumer) {
            // Subscribing to the topic
            consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC));
            while (true) {
                // Polling records
                ConsumerRecords<Long, GenericRecord> consumerRecords = consumer.poll(TEN_MILLISECONDS_INTERVAL);
                for (ConsumerRecord<Long, GenericRecord> cr : consumerRecords) {
                    // Logging received record
                    logger.info("Received record: key={}, value={}, partition={}, offset={}",
                            cr.key(), cr.value(), cr.partition(), cr.offset());
                }
            }
        }
    }
}
