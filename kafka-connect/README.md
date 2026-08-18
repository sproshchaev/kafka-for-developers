# kafka-connect: занятия 21 и 22

[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791??style=for-the-badge&logo=postgresql&logoColor=FFFFFF)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)

Один стенд на два занятия курса:

- **21. Kafka Connect. Простые коннекторы** — LIVE REST, Демо 1, Демо 2
- **22. Kafka Connect. Трансформации. Потоковый конвейер данных** — Демо 3, 4, 5, 6

Java-кода здесь нет: все шесть демо построены на готовых коннекторах и настраиваются
JSON-конфигурациями через REST API. Поэтому модуль не подключён в `settings.gradle.kts`.

## Стенд

```txt
zookeeper-connect   2181
kafka1              INTERNAL kafka1:19092   EXTERNAL localhost:9092
kafka2              INTERNAL kafka2:19093   EXTERNAL localhost:9093
kafka3              INTERNAL kafka3:19094   EXTERNAL localhost:9094
connect             http://localhost:8083
postgres            localhost:5432, postgres/postgres
kafdrop-connect     http://localhost:9000
```

Три брокера и внутренние порты 19092/19093/19094 взяты не случайно: именно эти адреса
стоят в командах на слайдах обоих занятий, команды копируются со слайда и работают как есть.

Служебным контейнерам Zookeeper и Kafdrop даны имена с суффиксом `-connect`, чтобы стенд
не конфликтовал с контейнерами других вебинаров репозитория. В командах со слайдов они
не используются.

## Что внутри Connect

Образ собирается локально из `connect/Dockerfile`, в него доустановлены:

| Плагин | Версия | Где нужен |
|---|---|---|
| `confluentinc/kafka-connect-jdbc` | 10.7.6 | Демо 3, 4, 5 |
| `debezium/debezium-connector-postgresql` | 2.5.4 | Демо 6 |
| `org.apache.kafka:connect-file` | 3.6.1 | Демо 2 |

FileStream-коннекторы забираются отдельным jar из Maven Central: в образ Confluent Platform
они не входят вовсе, и без этого `connector.class: FileStreamSource` не находится.

## База

`postgres/init.sql` выполняется один раз при первом старте контейнера:

- `public.clients` — 300 строк с детерминированными значениями. Строка `id = 262` нужна
  для команды `UPDATE ... WHERE id = 262` со слайда Демо 3;
- `inventory.customers` — четыре строки из примера Debezium, `id = 1001` для Демо 6,
  с `REPLICA IDENTITY FULL`, чтобы в событии CDC приходило и `before`, и `after`;
- `public.customers` намеренно не создаётся: её создаёт сам JDBC Sink в Демо 4 (`auto.create`).

## Запуск

```shell
docker compose build          # первый раз: собирает образ Connect с плагинами
docker compose up -d
docker compose ps
```

Порядок шагов каждого демо — в `actions.md` рядом и в `webinar-21/actions.md`,
`webinar-22/actions.md`.

## Структура

```txt
kafka-connect/
├── docker-compose.yaml
├── connect/Dockerfile          образ Connect с плагинами
├── postgres/init.sql           clients, inventory.customers
├── data/source.csv             источник для Демо 2
├── actions.md                  стенд: поднять, проверить, снять
├── webinar-21/
│   ├── actions.md              LIVE REST, Демо 1, Демо 2
│   └── connectors/             source.json, sink.json
└── webinar-22/
    ├── actions.md              Демо 3, 4, 5, 6
    ├── customers-messages.json входные сообщения для Демо 4
    └── connectors/             clients.json, clients-smt.json, customers.json, inventory.json
```

