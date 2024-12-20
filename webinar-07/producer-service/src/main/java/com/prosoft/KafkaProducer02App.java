package com.prosoft;

import com.prosoft.config.KafkaConfig;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Kafka producer-service (отправка GenericRecord объектов)
 */
public class KafkaProducer02App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer02App.class);
    private static final int MAX_MESSAGE = 10;
    private static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
    private static final String TOPIC = KafkaConfig.TOPIC;

    public static void main(String[] args) {
        try (KafkaProducer<Long, GenericRecord> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {

            // Подключение к Schema Registry и загрузка схемы
            SchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(SCHEMA_REGISTRY_URL, 10);
            String subject = TOPIC + "-value";
            String schemaString = schemaRegistryClient.getLatestSchemaMetadata(subject).getSchema();
            Schema schema = new Schema.Parser().parse(schemaString);

            for (int i = 0; i < MAX_MESSAGE; i++) {
                GenericRecord person = createPerson(i, schema);
                ProducerRecord<Long, GenericRecord> producerRecord = new ProducerRecord<>(TOPIC, (long) i, person);
                producer.send(producerRecord);
                logger.info("Отправлено сообщение: key-{}, value-{}", i, person);
            }
            logger.info("Отправка завершена.");
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }
    }

    private static GenericRecord createPerson(int index, Schema schema) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));

        // Создание GenericRecord
        GenericRecord person = new GenericData.Record(schema);
        person.put("id", (long) index);
        person.put("firstName", "FirstName-" + currentTime);
        person.put("lastName", "LastName" + index);
        person.put("age", 20 + index);

        return person;
    }
}

