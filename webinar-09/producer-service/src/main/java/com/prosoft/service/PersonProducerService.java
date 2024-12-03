package com.prosoft.service;

import com.prosoft.domain.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonProducerService {

    private static final String TOPIC = "person-topic";
    private final KafkaTemplate<String, Person> kafkaTemplate;

    public void sendPerson(Person person) {
        kafkaTemplate.send(TOPIC, String.valueOf(person.getId()), person);
    }
}
