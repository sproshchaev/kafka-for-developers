package com.prosoft;

import com.prosoft.config.KafkaConfig;
import com.prosoft.domain.Person;
import com.prosoft.domain.PersonBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Webinar-07: Kafka producer-service (отправка объектов класса Person)
 * Примечание: для генерации классов с использованием плагина avro-maven-plugin (см pom.xml) необходимо открыть проект
 * как: "producer-service/src/main/java/com/prosoft/KafkaProducerApp.java"
 */
public class KafkaProducerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerApp.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {

        try (KafkaProducer<Long, Person> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {

            for (int i = 0; i < MAX_MESSAGE; i++) {
                Person person = createPerson(i);
                ProducerRecord<Long, Person> producerRecord = new ProducerRecord<>(KafkaConfig.TOPIC, person.getId(), person);
                producer.send(producerRecord);
                logger.info("Отправлено сообщение: key-{}, value-{}", person.getId(), person);
            }
            logger.info("Отправка завершена.");
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }
    }

    private static Person createPerson(int index) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return PersonBuilder.doSpecificRecord((long) index, "FirstName-" + currentTime, "LastName" + index, 20 + index);
    }

}