# webinar-03: Kafka Consumer API
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

Репликация для повышения отказоустойчивости и масштабируемости системы.     
```

Создание топика topic3 с тремя партициями (см. [actions.md](actions.md))
```sh
docker exec -ti kafka1 /usr/bin/kafka-topics --create --topic topic3 --partitions 3 --replication-factor 1 --bootstrap-server localhost:9191
```

## ConsumerConfig

См. [KafkaConfig.java](consumer-service%2Fsrc%2Fmain%2Fjava%2Fcom%2Fprosoft%2Fconfig%2FKafkaConfig.java)  
    [KafkaConfig.java](producer-service%2Fsrc%2Fmain%2Fjava%2Fcom%2Fprosoft%2Fconfig%2FKafkaConfig.java)  


## Features list

```txt
Kafka Consumer API
------------------
1) Настройка репликации Kafka, Zookeeper, Kafdrop 

2) partition.assignment.strategy — это параметр конфигурации в Apache Kafka, который определяет стратегию распределения партиций топика между консюмерами в группе консюмеров (Consumer Group)
      - RangeAssignor;
      - RoundRobin;
      - StickyAssignor; 
      - CooperativeStickyAssignor; 

3)  rebalance:
      + heartbeat.interval.ms - интервал отправки "сигналов жизни" (по умолч. 3 сек);
      + session.timeout.ms - время ожидания ответа на "сигналы жизни" (по умолч. 10 сек);
      + max.poll.interval.ms - время за которое Консюмер должен вызывать .poll() (по умолч. 5 мин); 
      - вызов rebalance:
           - .consumer.subscribe(Arrays.asList("topic1", "topic2")) - подписка на топик; 
           - .close() - метод закрывает потребитель и освобождает все ресурсы;
           - .unsubscribe() - отменяет подписку потребителя на все топики;
           - .wakeup() - прерывает текущий вызов метода `poll()`, заставляя его немедленно выбросить WakeupException;
           - .pause() - используется для временной приостановки получения данных из определённых партиций топика;
      - ConsumerRebalanceListener - интерфейс, который позволяет определить, что делать в случае ребалансировки; 
      + partition.assignment.strategy - параметр определяющий стратегию выбора для перераспределения партиций (RangeAssignor (по умолчанию), RoundRobin, StickyAssignor, CooperativeStickyAssignor); 
      - group.instance.id - статическое членство (static membership).   
4) Offset:
      + Auto-commit и топик "__consumer_offset";
      - enable.auto.commit=false - отключение Auto-commit (топик "__consumer_offset" не используется);
      - Consumer.commitSync() - блокирующий метод (синхронный), используется для ручной записи оффсета Консюмером в топик "__consumer_offset";
      - Consumer.commitAsync() - неблокирующий метод (асинхронный), используется для ручной записи оффсета Консюмером в топик "__consumer_offset". Возможно использовать Callback;
      - Совместное использование: Consumer.commitAsync() - для основного чтения. Consumer.commitSync() - перед закрытием Consumer в (finally);
5) Consumer API configuration:
      + botstrap.servers - адрес брокера. "broker1:9093,broker2:9094";
      + key.deserializer - класс десерилизации ключа. Интерфейс org.apache.kafka.common.serialization.Deserializer; 
      - кастомный десериализатор ключей. Интерфейс org.apache.kafka.common.serialization.Deserializer. Методы: configure, deserialize и close;
      + value.deserializer - класс десериализации сообщения;
      + кастомный десериализатор значений. Интерфейс org.apache.kafka.common.serialization.Deserializer. Методы: configure, deserialize; 
      + group.id - идентификатор потребительской группы (consumer group);
      + enable.auto.commit. Параметр auto.commit.interval.ms (по умолч. 5 сек);
      - Consumer.poll(): 
            + max.partition.fetch.bytes - определяет максимальный размер данных, которые потребитель может запросить за один раз из каждой партиции (Значение по умолчанию составляет 1 мегабайт (1048576 байт)); 
            + max.poll.records - параметр контролирует максимальное количество записей, которое потребитель может получить в одном вызове метода `poll()`. Он позволяет ограничить количество сообщений, которое потребитель может обработать за один раз, что может быть полезно для контроля нагрузки на потребителя. (Значение по умолчанию: 500);
6) KafkaConsumer:
      + создание объекта Properties;
      + создание объекта KafkaConsumer;  
      + подписка на топики .subscribe(); 
      + чтение сообщений: ConsumerRecords, метод .poll();
      + закрытие .close();
      - закрытие consumer.close(Duration.ofSeconds(10)). 
```

## Demo's description

Класс [KafkaProducerApp](https://github.com/sproshchaev/kafka-for-developers/blob/base/webinar-03/producer-service/src/main/java/com/prosoft/KafkaProducerApp.java) выполняет роль Kafka продюсера, который отправляет объекты класса Person в указанный Kafka топик.
Конфигурация продюсера берется из класса KafkaConfig. KafkaProducerApp отправляет 10 сообщений (MAX_MESSAGE).
Для каждого сообщения создается объект класса Person с уникальными данными. Создается объект ProducerRecord, 
который содержит информацию о топике, ключе и значении (объекте Person).
Сообщения отправляются в Kafka с использованием метода producer.send(), который принимает объект ProducerRecord и 
анонимный класс Callback для обработки результата отправки.
При успешной отправке сообщения логируется информация о ключе, значении, партиции и смещении.  

Класс [KafkaConsumerApp](https://github.com/sproshchaev/kafka-for-developers/blob/base/webinar-03/consumer-service/src/main/java/com/prosoft/KafkaConsumerApp.java) выполняет роль Kafka-потребителя, предназначенное для чтения сообщений из Kafka-топика (topic3) 
с использованием клиентской библиотеки Apache Kafka. Класс настраивает и запускает несколько потребителей Kafka в отдельных 
потоках для параллельного чтения сообщений из Kafka-топика.
Каждый экземпляр потребителя подписывается на один топик (topic3) с помощью consumer.subscribe().
В бесконечном цикле (while (true)) каждый потребитель выполняет опрос записей ConsumerRecords<Long, Person> 
и для каждой полученной записи выаодит в лог: ключ, значение, раздел и смещение, с использованием SLF4J.  

```txt
webinar-03
├── consumer-service
│   ├── build
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com.prosoft
│       │   │       ├── config
│       │   │       │   └── KafkaConfig.java - настройки конфигурации потребителя Kafka
│       │   │       ├── deserializer
│       │   │       │   └── PersonDeserializer.java - десериализатор массива байт из Kafka в объекты типа Person
│       │   │       ├── domain
│       │   │       │   └── Person.java - класс-домен, представляющий отправляемые объекты
│       │   │       └── KafkaConsumerApp.java
│       │   └── resources
│       └── test
│           └── build.gradle.kts
├── producer-service
│   ├── build
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com.prosoft
│       │   │       ├── config
│       │   │       │   └── KafkaConfig.java - настройки конфигурации отправителя Kafka
│       │   │       ├── domain
│       │   │       │   └── Person.java - класс-домен, представляющий принмаемые объекты
│       │   │       ├── serializer
│       │   │       │   └── PersonSerializer.java - сериализатор объектов типа Person в массив байт
│       │   │       └── KafkaProducerApp
│       │   └── resources
│       └── test
│           └── build.gradle.kts
├── actions.md
├── build.gradle.kts
├── docker-compose.yaml
└── README.md
```
