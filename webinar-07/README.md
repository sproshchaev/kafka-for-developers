# webinar-07: Kafka Schema Registry
[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)
[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)

## Kafka cluster
```txt
1) Брокер #1
Порт PLAINTEXT_HOST://localhost:9093
2) zookeeper
Порт ZOOKEEPER_CLIENT_PORT: 2181
3) Kafdrop
Порт http://localhost:9000/
4) schema-registry
Порт http://localhost:8081/
```

## Features list
```txt
Kafka Schema Registry
---------------------
1) Структура и варианты сообщений: текстовый и бинарный форматы
2) Схема данных
3) Avro
  - формат сообщения 
  - варианты создания и обработки Avro-сообщений
  - методы: Generic, Reflection, Specific
  - варианты реализации Продюсера и Консюмера для: Generic Record, Specific Record, Reflection
  - типы данных: 
    - Примитивные: null, boolean, int, long, float, double, bytes, string
    - Комплексные: record, enum, array, map, union, fixed
4) Сравнение Avro, Protobuf, JSON
5)  Schema Registry 
  - идея и концепция регистра схем
  - стратегия совместимости схем: Backward Compatibility, Forward Compatibility, Full Compatibility, None
  - subject: 
    - key.subject.name.strategy
    - value.subject.name.strategy
  - стратегии присваивания subject:
    - Topic Name Strategy
    - Record Name Strategy
    - Topic Record Name Strategy
```

## Demo's description
```txt
webinar-07
├── consumer-service
│   ├── build
│   ├── src
│   │   ├── main
│   │   │   ├── avro
│   │   │   │   └── Person.avsc
│   │   │   ├── java
│   │   │   │   └── com.prosoft
│   │   │   │       ├── config
│   │   │   │       │   └── KafkaConfig.java
│   │   │   │       └── domain
│   │   │   │           └── KafkaConsumerApp.java
│   │   │   ├── resources
│   │   │   │   └── logback.xml
│   │   └── test
│   └── target
│       ├── generated-sources
│       │   └── avro
│       │       └── com.prosoft
│       │           └── domain
│       │               └── Person.java
│       ├── classes
│       └── annotations
│
├── producer-service
│   ├── src
│   │   ├── main
│   │   │   ├── avro
│   │   │   │   └── Person.avsc
│   │   │   ├── java
│   │   │   │   └── com.prosoft
│   │   │   │       ├── config
│   │   │   │       │   └── KafkaConfig.java
│   │   │   │       └── domain
│   │   │   │           ├── PersonBuilder.java
│   │   │   │           └── KafkaProducerApp.java
│   │   ├── resources
│   │   │   └── logback.xml
│   │   └── test
│   └── target
│       ├── generated-sources
│       │   └── avro
│       │       └── com
│       │           └── prosoft
│       │               └── domain
│       │                   └── Person.java
│       ├── classes
│       └── annotations
│
├── person-reflect.avro
├── pom.xml
├── rest-api.http
├── build.gradle.kts
├── docker-compose.yaml
└── README.md
```
