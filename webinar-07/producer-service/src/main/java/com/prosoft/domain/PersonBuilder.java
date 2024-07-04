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

public class PersonBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PersonBuilder.class);
    private static final String PERSON_AVCS = String.valueOf(Paths.get("src", "main", "avro", "Person.avsc").toAbsolutePath());

    public static void main(String[] args) throws IOException {
        // logger.info(getPersonFromGenericRecord(1L, "John", "Doe", 32));
        // generateAvroFileFromClassByReflectDatum();
        logger.info(doSpecificRecord(1L, "John", "Doe", 32).toString());
    }

    // (1) Generic
    private static GenericRecord getPersonFromGenericRecord(long id, String firstName, String lastName, int age) {

        Schema schema = null;

        try {
            schema = new Parser().parse(new File(PERSON_AVCS));
        } catch (IOException e) {
            //throw new RuntimeException(e);
            logger.error("Ошибка при загрузке схемы Avro", e);
        }

        GenericRecord person = new GenericData.Record(schema);
        person.put("id", id);
        person.put("firstName", firstName);
        person.put("lastName", lastName);
        person.put("age", age);
        return person;
    }

    // (2) Ref.
    public static void generateAvroFileFromClassByReflectDatum() throws IOException {
        Person person = new Person(1L, "John", "Doe", 32);

        Schema schema = ReflectData.get().getSchema(Person.class);

        File file = new File("person-reflect.avro");

        ReflectDatumWriter<Person> writer = new ReflectDatumWriter<>(schema);

        // try
        DataFileWriter<Person> dataFileWriter = new DataFileWriter<>(writer);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(person);
        logger.info("Запись завершена");
    }

    // (3)
    public static Person doSpecificRecord(long id, String firstName, String lastName, int age) {

        Person person = Person.newBuilder()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAge(age).build();
        return person;

    }

}
