# webinar-02: Kafka Producer API
[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)
[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)

## Kafka cluster
```txt
1) Брокер #1 
Порт PLAINTEXT_HOST://localhost:9091
2) Брокер #2 
Порт PLAINTEXT_HOST://localhost:9092
3) Брокер #3
Порт PLAINTEXT_HOST://localhost:9093 
4) zookeeper
Порт ZOOKEEPER_CLIENT_PORT: 2181
5) Kafdrop
Порт http://localhost:9000/

Репликация для повышения отказоустойчивости и масштабируемости системы:

Фактор репликации настроен на 3, что позволяет распределять данные по всем трем брокерам

1) Все брокеры имеют фактор репликации:
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3

Это означает, что:
- Партиции топиков будут реплицироваться на все три брокера.
- Топики с replication factor 3 будут устойчивы к сбоям до двух брокеров.

2) Минимальное количество синхронных реплик (ISR):
KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 2
Это означает, что минимум две реплики должны быть в синхронном состоянии для подтверждения операций. Это балансирует 
надежность и производительность.

3) Все три брокера связаны друг с другом через Zookeeper
```
## ProducerConfig
При использовании Kafka с несколькими брокерами, в конфигурации продюсера (ProducerConfig) нужно указать список всех 
брокеров, к которым продюсер может подключиться. Это делается для повышения отказоустойчивости: если один из брокеров 
недоступен, продюсер может отправить сообщения другому брокеру:  
```java
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091, localhost:9092, localhost:9093");  
    // ...
```

## Features list
```txt
Kafka Producer API
------------------
1) Настройка репликации Kafka, Zookeeper, Kafdrop 

2) Producer Record: 
   + Topic; 
   + Value (String); 
   - Байтовый массив (byte[]); 
   + Числовые типы (Integer, Long, Double и т.д.); 
   + Пользовательские объекты (Custom Objects): - JSON (джейсон); 
   + message.max.bytes - максимальный размер сообщения;
   + max.request.size - максимальный размер запроса, который может быть отправлен продюсером;
   + Ключ (Message Key);
   + Partition: new ProducerRecord<>(topic, partition, timestamp, key, value);
   + Timestamp: new ProducerRecord<>("my-topic", "key", "value", System.currentTimeMillis());
   - Headers: RecordHeaders headers = new RecordHeaders(), headers.add(new RecordHeader("header-key1", "header-value1".getBytes())), new ProducerRecord<>("my-topic", null, "key", "value", headers). 

3) Создание "пользовательского класса партицирования": implements Partitioner.  

4) Timestamp: CreateTime (Время создания), LogAppendTime (Время добавления в лог). 

5) Producer API configuration:
  + botstrap.servers - адрес брокера. "localhost:9091, localhost:9092, localhost:9093";
  + key.serializer - класс серилизации ключа;
  - кастомный сериализатор ключей: интерфейс Serializer;
  + value.serializer - класс сериализации сообщения;
  + compression.type - метод сжатия данных: "none" (без сжатия), "gzip" (используется алгоритм Gzip), "snappy" (используется алгоритм Snappy), "lz4" (используется алгоритм LZ4); 
  + acks - метод подтверждения доставки: `0` (нет подтверждений), `1` (подтверждение лидера), `all` (подтверждение от всех реплик);
  + delivery.timeout.ms - сколько времени может быть потрачено на отправку сообщения (или время возвращения результата от .send()) (по умолчанию 2 минуты).;
  + batch.size максимальный размер батча (по умолчанию: 16384 (16 КБ)), props.put("batch.size", 32768);
  + linger.ms - время ожидания батчем новых сообщений перед отправкой: props.put("linger.ms", 0) (По умолчанию 0);
  + max.block.ms - максимальное время блокировки;
  + delivery.timeout.ms - максимальное время ожидания подтверждения;
  + retry.backoff.time - время задержки перед повторной попыткой;
  + request.timeout.ms - это параметр конфигурации в Kafka Producer API, который определяет максимальное время ожидания ответа на запрос от брокера Kafka после отправки запроса.
  
6) KafkaProducer:
      + создание объекта Properties;
      + создание объекта KafkaProducer;  
      + отправка сообщения:
            - асинхронный .send();
            + обработка Callback;   
            - блокирующий producer.send(record).get();  
            - producer.flush() - сброса буферов и отправки всех накопленных сообщений.    
      - закрытие:
            - .close();
            - .close(Duration.ofSeconds(60)).
```

## Demo's description
Приложение KafkaProducerApp формирует 10 объектов класса Person, сериализует и отправляет в заданный топик Kafka.
Обработка результата отправки - в методе onCompletion интерфейса Callback проверяется наличие исключений и логируется 
отправка.

Приложение-потребитель KafkaConsumerApp (consumer) получает сообщения из заданного топика десериализует в объекты и 
логирует получение.

```txt
webinar-02/
│
├── consumer-service/
│   ├── build/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── com.prosoft/
│   │               ├── config/
│   │               │   └── KafkaConfig.java        - конфигуратор потребителя
│   │               ├── deserializer/
│   │               │   └── PersonDeserializer.java - кастомный десериализатор
│   │               ├── domain/
│   │               │   └── Person.java             - доменная сущность
│   │               └── KafkaConsumerApp.java       - потребитель
│   ├── resources/
│   └── test/
│       └── build.gradle.kts
│
└── producer-service/
    ├── build/
    ├── src/
    │   └── main/
    │       └── java/
    │           └── com.prosoft/
    │               ├── config/
    │               │   └── KafkaConfig.java      - конфигуратор производителя
    │               ├── domain/
    │               │   └── Person.java           - доменная сущность
    │               ├── serializer/
    │               │   └── PersonSerializer.java - кастомный сериализатор
    │               └── KafkaProducerApp.java     - производитель 
    ├── resources/
    ├── test/
    ├── build.gradle.kts
    ├── docker-compose.yaml                       - файл конфигурации для Docker Compose
    └── README.md
```
