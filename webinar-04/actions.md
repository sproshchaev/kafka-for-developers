[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/) 
# Apache Kafka shell actions

## Контейнеризация

`1.` Запуск контейнеров Docker:
```shell
docker compose up -d
```

`2.` Вывод списока всех контейнеров Docker (включая остановленные):
```shell
docker compose ps -a
```

`3.` Остановка контейнеров, удаление контейнеров, удаление неиспользуемых томов:
```shell
docker compose stop
docker container prune -f
docker volume prune -f
```

## Управление топиками

`1.` Получение списка топиков брокера Kafka (kafka-topics)
```shell
docker exec -ti kafka1 /usr/bin/kafka-topics --list --bootstrap-server localhost:9191
```

`2.` Создание топика "topic-name1" с одной партицией в брокере Kafka (kafka-topics)
```shell
docker exec -ti kafka1 /usr/bin/kafka-topics --create --topic topic-name1 --bootstrap-server localhost:9191
```

`3.` Создание топика "topic-name2" с числом партиций 3 в брокере Kafka (kafka-topics)
```shell
docker exec -ti kafka1 /usr/bin/kafka-topics --create --topic topic-name2 --partitions 3 --replication-factor 1 --bootstrap-server localhost:9191
```

`4.` Изменение топика "topic-name1": увеличение числа партиций до 4 в брокере Kafka (kafka-topics)
```shell
docker exec -ti kafka1 /usr/bin/kafka-topics --alter --topic topic-name1 --partitions 4 --bootstrap-server localhost:9191
```

`5.` Удаление топика "topic-name1" в брокере Kafka (kafka-topics)
```shell
docker exec -ti kafka1 /usr/bin/kafka-topics --delete --topic topic-name1 --bootstrap-server localhost:9191
```

## Отправка и получение сообщений

`1.` Создать сообщение в топике "topic-name1" брокера Kafka (kafka-console-producer)
```shell
docker exec -ti kafka1 /usr/bin/kafka-console-producer --topic topic-name1 --bootstrap-server localhost:9191
```

`2.` Прочитать все сообщения с самого начала из топика "topic-name1" брокера Kafka (kafka-console-consumer)
```shell
docker exec -ti kafka1 /usr/bin/kafka-console-consumer --topic topic-name1 --from-beginning --bootstrap-server localhost:9191
```

`3.` Прочитать все сообщения с текущего оффсета топика "topic-name1" брокера Kafka (kafka-console-consumer)
```shell
docker exec -ti kafka1 /usr/bin/kafka-console-consumer --topic topic-name1 --bootstrap-server localhost:9191
```

## Администрирование и мониторинг

`1.` Получение списка топиков брокера Kafka (kafka-topics)
```shell
docker exec -ti kafka1 /usr/bin/kafka-topics --list --bootstrap-server localhost:9191
```

`2.` Получения информации о топике брокера Kafka (kafka-topics)
```shell
docker exec -ti kafka1 /usr/bin/kafka-topics --describe --topic topic-name1 --bootstrap-server localhost:9191
```

`3.` Получение списока групп потребителей брокера Kafka (kafka-consumer-groups)
```shell
docker exec -ti kafka1 /usr/bin/kafka-consumer-groups --list --bootstrap-server localhost:9191
```

`4.` Получение описания всех групп потребителей брокера Kafka (kafka-consumer-groups)
```shell
docker exec -ti kafka1 /usr/bin/kafka-consumer-groups --describe --all-groups --bootstrap-server localhost:9191
```

## Автоматизация и скриптинг

(Требует прав администратора)

`1.` Список содержимого /usr/bin
```shell
docker exec kafka1 ls -l /usr/bin
```

`2.` Создание файла create_topic.sh 
```shell
sudo tee /bin/bash/create_topic.sh > /dev/null << 'EOF'
#!/bin/bash
# create_topic.sh

TOPIC_NAME=\$1
PARTITIONS=\$2
REPLICATION_FACTOR=\$3
BROKER_LIST=\$4

kafka-topics.sh --create --topic \$TOPIC_NAME --partitions \$PARTITIONS --replication-factor \$REPLICATION_FACTOR --bootstrap-server \$BROKER_LIST
EOF

sudo chmod +x /bin/bash/create_topic.sh
```

`3.` Запуск созданного скрипта create_topic.sh
``` shell
docker exec kafka1 /bin/bash/create_topic.sh topic-name3 2 1 localhost:9191
```
