package com.prosoft.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * Webinar-05: KafkaConfig содержит конфигурацию для транзакционного консюмера в виде метода getConsumerConfig.
 * Конфигурации включают настройки для серверов Kafka, десериализации и групп потребителей.
 */
public class KafkaConfig02 {

    public static final String TOPIC = "topic1";

    private static final String BOOTSTRAP_SERVERS = "localhost:9093";
    private static final String GROUP_ID = "my-consumer-group";

    private KafkaConfig02() {
    }

    public static Properties getConsumerConfig() {
        Properties properties = new Properties();
        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Устанавливает уровень изоляции для потребителя и определяет, какие сообщения потребитель может видеть в теме:
         * - read_uncommitted - потребитель видит все доступные сообщения в теме, включая незафиксированные сообщения;
         * - read_committed - потребитель видит только те сообщения, которые были успешно зафиксированы в теме.
         */
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        /** Идентификатор группы потребителей (consumer group ID) */
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        /** Использование StringSerializer для десериализации ключей и значений сообщений.
         *  StringSerializer.class в контексте Apache Kafka представляет собой реализацию интерфейса Serializer
         *  из клиентской библиотеки Kafka, которая используется для сериализации объектов типа String в байтовый формат.
         */
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        /** Управление поведением потребителя при первом подключении к топику или при потере сохраненного смещения:
         * - "earliest": начинает считывать сообщения с самого начала топика
         * - "latest": начинает считывать сообщения с самого последнего доступного смещения.
         * - "none": если нет сохраненного смещения, потребитель выбрасывает исключение.
         * - "error": потребитель выбрасывает исключение при отсутствии сохраненного смещения или если смещение находится
         * за пределами диапазона доступных смещений.
         * */
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }
}
