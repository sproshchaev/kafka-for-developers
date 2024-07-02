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

public class CreateTopics {

    private static final Logger logger = LoggerFactory.getLogger(CreateTopics.class);

    public static void main(String[] args) {
        createTopics();
        createWithKafkaFuture();
    }

    private static void createTopics() {

        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            String topicName = "my-topic1";
            String topicName2 = "my-topic2";
            String topicName3 = "my-topic3";

            int numPartitions = 3;

            short replicationFactor = 1;

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            logger.info("Топик '{}' успешно создан.", topicName);

            List<NewTopic> topics = Arrays.asList(new NewTopic(topicName2, numPartitions, replicationFactor),
                    new NewTopic(topicName3, numPartitions, replicationFactor));
            adminClient.createTopics(topics).all().get();
            logger.info("Топики из списка '{}' успешно созданы.", Arrays.toString(topics.toArray()));

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void createWithKafkaFuture() {

        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            String topicName = "my-topic4";
            int numPartitions = 3;
            short replicationFactor = 2;

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));

            KafkaFuture<Void> future = createTopicsResult.all();

            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    logger.info("Topic created successfully.");
                } else {
                    logger.error("Failed to create topic", exception);
                }
            });

            future.get();


        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
