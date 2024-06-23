package com.prosoft;

import com.prosoft.config.KafkaConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Simple creation of topics
 */
public class CreateTopics {

    private static final Logger logger = LoggerFactory.getLogger(CreateTopics.class);

    public static void main(String[] args) {
        simpleCreate();
        createWithKafkaFuture();
    }

    /**
     * Простое создание Топиков
     */
    private static void simpleCreate() {
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Название создаваемых топиков */
            String topicName = "my-topic";
            String topicName2 = "my-topic2";
            String topicName3 = "my-topic3";

            /** Количество партиций для топика */
            int numPartitions = 3;

            /** Фактор репликации для топика (replication factor) определяет, сколько копий (реплик) каждого раздела (partition)
             * топика хранится на различных брокерах Kafka в кластере.
             * Каждый раздел в Kafka имеет одного лидера (leader) и ноль или более реплик (replicas).
             * Пример значений replication factor:
             *   0 - не будет никаких реплик для разделов топика, данные каждого раздела будут храниться только на одном брокере, который и является лидером раздела;
             *   1 - каждый раздел топика будет иметь одну реплику, которая является лидером. В этом случае отказоустойчивость не обеспечивается, так как нет дублирования данных на других брокерах;
             *   2 - означает, что каждый раздел топика будет иметь две реплики: одна из них будет лидером, а вторая будет репликой. Это обеспечивает отказоустойчивость, так как данные будут доступны для чтения и записи, даже если один из брокеров станет недоступным.
             */
            short replicationFactor = 1;

            /** Создание еденичного топика */
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            logger.info("Топик '{}' успешно создан.", topicName);

            /** Создание топиков из коллекции */
            List<NewTopic> topics = Arrays.asList(new NewTopic(topicName2, numPartitions, replicationFactor),
                    new NewTopic(topicName3, numPartitions, replicationFactor));
            adminClient.createTopics(topics).all().get();
            logger.info("Топики из списка '{}' успешно созданы.", Arrays.toString(topics.toArray()));

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при создании топика", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Создание Топика с использованием KafkaFuture
     */
    private static void createWithKafkaFuture() {

        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Кол-во партиций для топика */
            int numPartitions = 3;

            /** Фактор репликации для топика (replication factor)
             * 2 - означает, что каждый раздел топика будет иметь две реплики: одна из них будет лидером, а вторая будет репликой. Это обеспечивает отказоустойчивость, так как данные будут доступны для чтения и записи, даже если один из брокеров станет недоступным.
             */
            short replicationFactor = 2;

            NewTopic newTopic = new NewTopic("my-topic1", numPartitions, replicationFactor);
            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));

            KafkaFuture<Void> future = createTopicsResult.all();

            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    logger.info("Topic created successfully.");
                } else {
                    logger.error("Failed to create topic", exception);
                }
            });

            /**
             * Блокирующий вызов для демонстрации
             */
            future.get();

        } catch (Exception e) {
            logger.error("Error creating Kafka topic", e);
            Thread.currentThread().interrupt();
        }
    }

}
