package com.prosoft;

import com.prosoft.config.KafkaConfig02;
import com.prosoft.domain.Person;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KafkaProducer02App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer02App.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {

        try (KafkaProducer<Long, Person> producer = new KafkaProducer<>(KafkaConfig02.getProducerConfig())) {

            // (!)
            producer.initTransactions();
            logger.info("(1) .initTransactions()");

            // (!)
            producer.beginTransaction();
            logger.info("(2) .beginTransaction()");

            for (int i = 0; i < MAX_MESSAGE; i++) {

                try {

                    Person person = createPerson(i);
                    ProducerRecord<Long, Person> producerRecord = new ProducerRecord<>(KafkaConfig02.TOPIC, person.getId(), person);

                    // (!)
                    RecordMetadata metadata = producer.send(producerRecord).get();

                    logger.info("Отправлено сообщение: key-{}, value-{}, offset:-{}", person.getId(), person, metadata.offset());


                } catch (Exception e) {

                    // (!)
                    producer.abortTransaction();
                    logger.error(".abortTransaction(): ошибка при отправке сообщения в Kafka", e);

                }

            }

            // (!)
            producer.commitTransaction();
            logger.info("(3) .commitTransaction()");

        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }

    }

    private static Person createPerson(int index) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return new Person(index, "FirstName-" + currentTime, "LastName" + index, 20 + index);
    }

}
