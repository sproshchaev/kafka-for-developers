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

public class KafkaProducerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerApp.class);


    public static void main(String[] args) {

        KafkaProducer kafkaProducer = new KafkaProducer(KafkaConfig.getProducerConfig());

        Person person = createPerson(1);
        ProducerRecord<Long, Person> producerRecord = new ProducerRecord<>(KafkaConfig.TOPIC, person.getId(), person);
        kafkaProducer.send(producerRecord);
        logger.info("Отправлено сообщение: key-{}, value-{}", person.getId(), person);

    }

    private static Person createPerson(int index) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return PersonBuilder.doSpecificRecord((long) index, "FirstName-" + currentTime, "LastName" + index, 20 + index);
    }

}