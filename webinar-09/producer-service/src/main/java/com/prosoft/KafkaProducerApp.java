package com.prosoft;

import com.prosoft.domain.Person;
import com.prosoft.service.PersonProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class KafkaProducerApp implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerApp.class);
    private static final int MAX_MESSAGE = 10;

    @Autowired
    private PersonProducerService personProducerService;

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < MAX_MESSAGE; i++) {
            Person person = createPerson(i);

            try {
                personProducerService.sendPerson(person);
                logger.info("Отправлено сообщение: key={}, value={}", person.getId(), person);
            } catch (Exception e) {
                logger.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
            }
        }
        logger.info("Отправка завершена.");
    }

    private static Person createPerson(int index) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return new Person(
                index,
                "FirstName-" + currentTime,
                "LastName-" + index,
                20 + index
        );
    }

}
