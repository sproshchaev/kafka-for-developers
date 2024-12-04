package com.prosoft;

import com.prosoft.config.KafkaConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс CreateTopics содержит статические методы работы с Топиками через AdminClient
 */
public class CreateTopics {

    private static final Logger logger = LoggerFactory.getLogger(CreateTopics.class);

    public static void main(String[] args) {
        KafkaAdminApp.deleteAllTopics();
        simpleCreate();
        createWithKafkaFuture();
        validateTopicCreation("my-topic4");
        createTopicWithCustomReplication();
    }

    /**
     * Простое создание Топиков
     */
    private static void simpleCreate() {

        /** Создание AdminClient с использованием конфигурации из KafkaConfig и блока try-with-resources */
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

            /** Создание единичного топика */
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
     * Метод createWithKafkaFuture() предназначен для создания топика в Kafka с использованием KafkaFuture
     * для асинхронной обработки результата
     */
    private static void createWithKafkaFuture() {

        /** Создание AdminClient с использованием конфигурации из KafkaConfig и блока try-with-resources */
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Название создаваемого топика */
            String topicName = "my-topic4";

            /** Кол-во партиций для топика */
            int numPartitions = 3;

            /** Фактор репликации для топика (replication factor): 2 - означает, что каждый раздел топика будет иметь
             * две реплики: одна из них будет лидером, а вторая будет репликой. Это обеспечивает отказоустойчивость,
             * так как данные будут доступны для чтения и записи, даже если один из брокеров станет недоступным.*/
            short replicationFactor = 2;

            /** Создается объект NewTopic с указанными параметрами */
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            /** Формирование запроса в AdminClient на создание топика с использованием метода .createTopics() */
            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));

            /** Получение KafkaFuture: возвращается объект KafkaFuture, который является асинхронным результатом создания топика.
             * Метод .all() в классе CreateTopicsResult возвращает единый `KafkaFuture<Void>`, который завершится успешно,
             * если все топики были успешно созданы.
             * Если создание любого из топиков завершится с ошибкой, этот `KafkaFuture` также завершится с ошибкой. */
            KafkaFuture<Void> future = createTopicsResult.all();

            /** Обработка результата создания топика:
             *  Метод whenComplete используется для обработки результата создания топика:
             *  1) если exception равен null, значит, топик успешно создан, и выводится сообщение об успехе;
             *  2) если exception не равен null, значит, произошла ошибка, и выводится сообщение об ошибке с информацией
             *  о возникшем исключении.
             *  */
            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    logger.info("Topic created successfully.");
                } else {
                    logger.error("Failed to create topic", exception);
                }
            });

            /**
             * Блокирующий вызов для демонстрации.
             * Этот вызов блокирует выполнение потока до завершения асинхронной операции создания топика.
             * Здесь это используется для демонстрации, но в реальных приложениях лучше избегать блокирующих вызовов.
             */
            future.get();

        } catch (Exception e) {
            logger.error("Error creating Kafka topic", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Метод validateTopicCreation() проверяет возможность создания топика в Apache Kafka.
     * Использует AdminClient для асинхронной проверки возможности создания топика с указанным именем.
     * Возвращает true, если топик может быть создан, иначе false.
     *
     * @param topicName Название топика, который требуется проверить на возможность создания.
     * @return true, если топик может быть создан; false, если создание топика невозможно.
     */
    private static boolean validateTopicCreation(String topicName) {

        /** Использование атомарной переменной обеспечивает безопасность работы с данными в многопоточных приложениях. */
        AtomicBoolean isValid = new AtomicBoolean(false);

        /** Создание AdminClient с использованием конфигурации из KafkaConfig и блока try-with-resources */
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Количество партиций для топика */
            int numPartitions = 3;

            /** Фактор репликации для топика */
            short replicationFactor = 1;

            /** Создание объекта NewTopic */
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            /** Создание объекта CreateTopicsOptions и установка validateOnly в true
             * Если установлено в true, то метод будет только проверять, могут ли топики быть созданы, без фактического их создания.
             */
            CreateTopicsOptions options = new CreateTopicsOptions().validateOnly(true);

            /** Вызов метода createTopics с установленными опциями */
            adminClient.createTopics(Collections.singleton(newTopic), options).all().whenComplete((results, exception) -> {
                if (exception == null) {
                    logger.info("Проверка прошла успешно: Топик может быть создан.");
                    isValid.set(true);
                } else {
                    logger.error("Ошибка проверки: Топик не может быть создан", exception);
                    isValid.set(false);
                }
            }).get();

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при проверке Топика", e);
            Thread.currentThread().interrupt();
        }
        return isValid.get();
    }

    /**
     * Создание топика с ручным заданием репликации для каждой партиции
     */
    private static void createTopicWithCustomReplication() {
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {
            String topicName = "my-topic5";

            /**
             * Map с указанием репликации для каждой партиции
             */
            Map<Integer, List<Integer>> partitionReplicas = Map.of(
                    0, List.of(2, 3),     // Партиция 0 с репликами на брокерах 2 и 3
                        1, List.of(2, 3)      // Партиция 1 с репликами на брокерах 2 и 3
            );

            NewTopic newTopic = new NewTopic(topicName, partitionReplicas);

            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));
            KafkaFuture<Void> future = createTopicsResult.all();

            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    logger.info("Топик '{}' успешно создан с репликацией: {}", topicName, partitionReplicas);
                } else {
                    logger.error("Ошибка при создании топика '{}'", topicName, exception);
                }
            });

            future.get();

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при создании топика", e);
            Thread.currentThread().interrupt();
        }
    }

}
