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
5) schema-registry-ui
Порт http://localhost:8001/
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
├── schema-registry.postman_collection.json — коллекция запросов Schema Registry для Postman
├── build.gradle.kts
├── docker-compose.yaml — конфигурационный файл для автоматизации развертывания Docker-приложений
└── README.md
```
---
Примечание:  
1. В ветке `base` находятся примеры с использованием специфичного (specific) класса, который создается с использованием  
плагина Maven из схемы Avro (плагин генерирует `domain\Person.java`, см. настройку `specific.avro.reader=true`)  
2. В ветке https://github.com/sproshchaev/kafka-for-developers/blob/feature/add-person-schema-registry/webinar-07/README.md находится 
пример с использованием `generic Avro Reader` вместо специфичного (specific) класса (поэтому нет `domain\Person.java`, см. настройку `specific.avro.reader=false`).  
Как это работает: перед публикацией сообщения регистрируется схема в Schema Registry (через REST API) и формируется её ID.
Если мы не указываем id версии в явную, то при запросе мы получаем самую последнюю зарегистрированную версию схемы в Schema Registry. 
Но мы можем указать конкретную версию:
```java
    private static final int SCHEMA_VERSION = 2;
    String schemaString = schemaRegistryClient.getSchemaMetadata(subject, SCHEMA_VERSION).getSchema();
    Schema schema = new Schema.Parser().parse(schemaString);
    //...
```
Этот ID схемы вставляется в начало каждого Kafka-сообщения. Когда сообщение потребляется, KafkaAvroDeserializer считывает ID схемы  
из сообщения и извлекает соответствующую схему из Schema Registry.

---

## Описание REST API Confluent Schema Registry для управления схемами 

## **1. Управление схемами и субъектами**

### **1.1 Добавление новой схемы или версии схемы** ✅

- **POST /subjects/{subject}/versions**
- **Описание**: Регистрирует новую схему или добавляет новую версию схемы для указанного субъекта.
- **Тело запроса**:

    ```json
    {
      "schema": "{\"type\":\"record\",\"name\":\"test\",\"fields\":[{\"name\":\"f1\",\"type\":\"string\"}]}"
    }
    ```

- **Ответ**:

    ```json
    {
      "id": 1
    }
    ```


---

### **1.2 Получение всех субъектов** ✅

- **GET /subjects**
- **Описание**: Возвращает список всех субъектов (subjects), зарегистрированных в Schema Registry.

---

### **1.3 Получение всех версий схемы для субъекта** ✅

- **GET /subjects/{subject}/versions**
- **Описание**: Возвращает список всех версий схемы для указанного субъекта.

---

### **1.4 Получение конкретной версии схемы** ✅

- **GET /subjects/{subject}/versions/{version}**
- **Описание**: Возвращает схему для определенной версии субъекта.
- **Пример**: `GET /subjects/test-subject/versions/latest`

---

### **1.5 Удаление схемы (субъекта)** ✅

- **DELETE /subjects/{subject}**
- **Описание**: Удаляет все версии схемы для указанного субъекта.

---

### **1.6 Удаление конкретной версии схемы** ✅

- **DELETE /subjects/{subject}/versions/{version}**
- **Описание**: Удаляет конкретную версию схемы для субъекта.

---

## **2. Получение и проверка схем**

### **2.1 Получение схемы по ID** ✅

- **GET /schemas/ids/{id}**
- **Описание**: Возвращает схему по ее уникальному ID.

---

### **2.2 Проверка совместимости схемы** ✅

- **POST /compatibility/subjects/{subject}/versions/{version}**
- **Описание**: Проверяет, совместима ли новая схема с указанной версией.
- **Тело запроса**:

    ```json
    {
      "schema": "{\"type\":\"record\",\"name\":\"test\",\"fields\":[{\"name\":\"f1\",\"type\":\"string\"}]}"
    }
    ```


---

### **2.3 Получение совместимости для субъекта** ✅

- **GET /config/{subject}**
- **Описание**: Получает текущий уровень совместимости (compatibility level) для конкретного субъекта.

---

### **2.4 Установка уровня совместимости** ✅

- **PUT /config/{subject}**
- **Описание**: Устанавливает уровень совместимости для конкретного субъекта.
- **Тело запроса**:

    ```json
    {
      "compatibility": "BACKWARD"
    }
    ```


---

## **3. Управление глобальными настройками**

### **3.1 Получение глобального уровня совместимости** ✅

- **GET /config**
- **Описание**: Возвращает текущий глобальный уровень совместимости.

---

### **3.2 Установка глобального уровня совместимости** ✅

- **PUT /config**
- **Описание**: Устанавливает глобальный уровень совместимости для всех субъектов.
- **Тело запроса**:

    ```json
    {
      "compatibility": "FULL"
    }
    ```


---

## **4. Поиск схем**

### **4.1 Получение версии схемы по содержимому** ✅

- **POST /subjects/{subject}**
- **Описание**: Возвращает версию схемы, если такая схема уже зарегистрирована.
- **Тело запроса**:

    ```json
    {
      "schema": "{\"type\":\"record\",\"name\":\"test\",\"fields\":[{\"name\":\"f1\",\"type\":\"string\"}]}"
    }
    ```


---

### **4.2 Проверка, существует ли схема** ✅ (1)

- **POST /subjects/{subject}/versions**
- **Описание**: Регистрирует схему или возвращает её версию, если она уже существует.