package com.prosoft;

import com.prosoft.config.KafkaConfig;
import com.prosoft.domain.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

public class KafkaConsumerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApp.class);
    private static final Duration TEN_MILLISECONDS_INTERVAL = Duration.ofMillis(10);

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {

            new Thread(() -> {

                KafkaConsumer<Long, Person> consumer = new KafkaConsumer<>(KafkaConfig.getConsumerConfig());

                try {consumer.subscribe(Collections.singleton("topic3"));

                    while (true) {
                        ConsumerRecords<Long, Person> record = consumer.poll(TEN_MILLISECONDS_INTERVAL);
                        for (ConsumerRecord<Long, Person> record1 : record) {
                            logger.info(record1.key() + ": " + record1.value());
                        }
                    }

                } catch (Exception e) {
                    logger.error("Exception occurred in consumer thread", e);
                } finally {
                    consumer.close();
                }
            }).start();

        }
    }

}