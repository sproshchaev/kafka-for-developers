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
 * Указание субъекта 'w7-topic-value' и ID схемы (в вашем случае 1) происходит автоматически благодаря интеграции
 * KafkaAvroDeserializer с Schema Registry.
 * Продюсер вставляет id схемы в начало каждого Kafka-сообщения и когда сообщение обрабатывает потребитель,
 * то KafkaAvroDeserializer считывает id схемы из сообщения и извлекает её из Schema Registry.
 */
public class KafkaConsumer02App {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer02App.class);
    private static final Duration TEN_MILLISECONDS_INTERVAL = Duration.ofMillis(10);

    public static void main(String[] args) {
        // Создание KafkaConsumer с исп. GenericRecord
        KafkaConsumer<Long, GenericRecord> consumer = new KafkaConsumer<>(KafkaConfig.getConsumerConfig());
        try (consumer) {
            // Подписываемся на наш топик
            consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC));
            while (true) {
                // Получаем сообщения
                ConsumerRecords<Long, GenericRecord> consumerRecords = consumer.poll(TEN_MILLISECONDS_INTERVAL);
                for (ConsumerRecord<Long, GenericRecord> cr : consumerRecords) {
                    logger.info("Received record: key={}, value={}, partition={}, offset={}",
                            cr.key(), cr.value(), cr.partition(), cr.offset());
                }
            }
        }
    }
}
