`1.` Запуск контейнеров Docker:
```shell
docker compose up -d
```

`2.` Вывод списка всех контейнеров Docker (включая остановленные):
```shell
docker compose ps -a
```

`3.` Получаем список Топиков которые есть в Kafka брокере, доступном по адресу kafka:9092 и находящемся внутри контейнера Docker с именем kafka:
```shell
docker exec -ti kafka /usr/bin/kafka-topics --list --bootstrap-server kafka:9093
```

`4.` Создаем новый "topic1"
```shell
docker exec -ti kafka /usr/bin/kafka-topics --create --topic topic1 --bootstrap-server localhost:9093
```

`5.` Отправляем сообщение "topic1": появляется консоль ввода сообщений, вводим сообщение одно за другим, разделяя Enter и в конце нажимаем в Win ctrl+D (в MacOS: control+C)
```shell
docker exec -ti kafka /usr/bin/kafka-console-producer --topic topic1 --bootstrap-server kafka:9093
```

`6.` Получить сообщения
```shell
docker exec -ti kafka /usr/bin/kafka-console-consumer --from-beginning --topic topic1 --bootstrap-server localhost:9093
```

`7.` Получить сообщения как consumer1
```shell
docker exec -ti kafka /usr/bin/kafka-console-consumer --group consumer1 --topic topic1 --bootstrap-server localhost:9093 
```

`8.` Останавливаем контейнеры, удаляем контейнеры, удаляем неиспользуемые тома:
```shell
docker compose stop
docker container prune -f
docker volume prune -f
```
