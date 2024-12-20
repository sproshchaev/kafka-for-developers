package com.prosoft.domain;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Служебный класс для создания записей Avro, представляющих объект Person.
 * 1) Метод Generic (getPersonFromGenericRecord) - мы вручную пишем схему в формате JSON: описываем поля и далее при помощи
 * библиотеки Avro создаем сообщения.
 * 2) Метод Reflection (generateAvroFileFromClassByReflectDatum) - на основе класса Java, который есть в проекте можно
 * сгенерировать схему в JSON.
 * 3) Метод Specific (doSpecificRecord) - (самый распространенный) когда у нас есть схема JSON и используя плагин Avro
 * можно генерировать классы.
 */
public class PersonBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PersonBuilder.class);

    private static final String PERSON_AVCS = String.valueOf(Paths.get("src", "main", "avro", "Person.avsc").toAbsolutePath());

    private PersonBuilder() {
    }

    public static void main(String[] args) {
        getPersonFromGenericRecord(1L, "John", "Doe", 32);
        generateAvroFileFromClassByReflectDatum();
        doSpecificRecord(1L, "John", "Doe", 32);
    }

    /**
     * GenericRecord позволяет работать с Avro без необходимости компилировать схему в Java классы и создает объект
     * в формате  Avro, который можно сериализировать и отправлять в Kafka.
     * @param id
     * @param firstName
     * @param lastName
     * @param age
     * @return GenericRecord - это объект в формате Avro, который можно сериализировать и отправлять в Kafka
     */
    private static GenericRecord getPersonFromGenericRecord(long id, String firstName, String lastName, int age) {

        /** Загрузка схемы из файла */
        Schema schema = null;
        try {
            schema = new Parser().parse(new File(PERSON_AVCS));
        } catch (IOException e) {
            logger.error("Ошибка при загрузке схемы Avro", e);
        }

        /** Создание экземпляра GenericRecord */
        GenericRecord person = new GenericData.Record(schema);
        person.put("id", id);
        person.put("firstName", firstName);
        person.put("lastName", lastName);
        person.put("age", age);
        logger.info("Создан GenericRecord для Person: {}", person);
        return person;
    }

    /***
     * Метод generateAvroFileFromClassByReflectDatum позволяет работать с Avro, используя классы POJO (Plain Old Java Object),
     * при этом не требуется генерировать код из Avro схем.
     * Если есть в ручную написанный класс (доменная модель), создан экземпляр этого класса, то используя этот метод можно
     * сгенерировать схему в формате Avro (без необходимости ее писать вручную, как в getPersonFromGenericRecord)
     */
    public static void generateAvroFileFromClassByReflectDatum() {

        /** Создание экземпляра класса Person */
        Person person = new Person();

        /** Получение схемы */
        Schema schema = ReflectData.get().getSchema(Person.class);

        /** Запись объекта в файл */
        File file = new File("person-reflect.avro");
        ReflectDatumWriter<Person> writer = new ReflectDatumWriter<>(schema);
        try (DataFileWriter<Person> dataFileWriter = new DataFileWriter<>(writer)) {
            dataFileWriter.create(schema, file);
            dataFileWriter.append(person);
        } catch (IOException e) {
            logger.error("Ошибка при записи объекта в файл Avro", e);
        }
        logger.info("ReflectDatum запись завершена.");
    }

    /**
     * Метод Specific (самый распространенный) использует автоматически сгенерированные классы из схемы Person.avsc
     * Использует файл PERSON_AVCS.
     * Класс генерируется:
     * 1) Меню "Maven" - "producer-service" - "Plugins" - "avro" - "avro:schema"
     * 2) с помощью команды mvn avro:schema
     * 3) mvn package
     *
     * Результат находится в папке target/generated-sources/avro/com/prosoft/domain/Person.java и доступен в classpath
     * и его можно использовать в import com.prosoft.domain.Person.
     * Этот созданный класс Person адаптирован под использование с Avro, и его далее можно использовать в коде, записывать
     * значения и сериализовывать перед отправкой в Kafka.
     * @return Person
     */
    public static Person doSpecificRecord(long id, String firstName, String lastName, int age) {
        /** Создание объекта Person (impl. SpecificRecord) */
        Person person = Person.newBuilder()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAge(age).build();
        logger.info("Создан объект Person с использованием SpecificRecord: {}", person);
        return person;
    }



}
