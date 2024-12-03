package com.prosoft.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * Webinar-01: KafkaConfig содержит конфигурацию для консюмера в виде метода getConsumerConfig.
 * Конфигурации включают настройки для серверов Kafka, десериализации и групп потребителей.
 */
public class KafkaConfig {

    public static final String TOPIC = "topic1";

    private static final String BOOTSTRAP_SERVERS = "localhost:9093";
    private static final String GROUP_ID = "my-consumer-group";

    private KafkaConfig() { }

    public static Properties getConsumerConfig() {
        Properties properties = new Properties();
        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Идентификатор группы потребителей (consumer group ID) */
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        /** Использование StringDeserializer для десериализации ключей и значений сообщений.
         *  StringDeserializer.class в контексте Apache Kafka представляет собой реализацию интерфейса Deserializer
         *  из клиентской библиотеки Kafka, которая используется для десериализации объектов типа из байтового формата
         *  в тип String.
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
