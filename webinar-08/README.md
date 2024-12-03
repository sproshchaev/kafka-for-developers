# webinar-08: Confluent REST API
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
5) kafka-rest
Порт http://localhost:8082
```

## Features list
```txt
Confluent REST API
------------------
1) Введение в Confluent REST API: 
  - история и назначение
  - примеры использования
  - основные функции
  - действующие ограничения
2) Развертывание Confluent REST API
  - Требования к аппаратной части
  - Особенности авторизации
3) Выполнение запросов:
  - Получение списка топиков
  - Отправка сообщения в топик
  - Чтение сообщений из топика: создание консюмера, подписка на топик, чтение сообщений из топика
  - Удаление консюмера
```

## Demo's description
```txt
webinar-08
├── docker-compose.yaml
├── README.md
└── rest-api.http
```
