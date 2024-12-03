package com.prosoft.service;

import com.prosoft.domain.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PersonConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(PersonConsumerService.class);

    @KafkaListener(topics = "person-topic", groupId = "person-consumer-group")
    public void consumePersonMessage(ConsumerRecord<String, Person> record) {
        String key = record.key();
        Person person = record.value();
        logger.info("Получено сообщение: key={}, value={}", key, person);
    }
}
