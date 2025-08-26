package com.prosoft.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.util.Properties;

/**
 * Webinar-07: KafkaConfig содержит конфигурацию для консюмера в виде метода getConsumerConfig.
 */
public class KafkaConfig {

    public static final String TOPIC = "w7-topic";

    private static final String BOOTSTRAP_SERVERS = "localhost:9093";
    private static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";

    private static final String GROUP_ID = "my-consumer-group";

    private KafkaConfig() {
    }

    public static Properties getConsumerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Идентификатор группы потребителей (consumer group ID) */
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        /** Использование LongDeserializer для десериализации ключей (Key) */
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());

        /** Использование KafkaAvroDeserializer для десериализации значения (Value) в формате Avro позволяет автоматически
         *  загружать схемы из Schema Registry. Зависимость kafka-avro-serializer (7.4.0) из репозитория https://packages.confluent.io/maven/ */
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());

        /** Адрес Schema Registry */
        properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);

        /** Настройка десериализатора (Avro) использовать generic Avro Reader для десериализации вместо специфичного (specific) класса.
         * Значение "false" означает использование generic reader.
         */
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "false");

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
