# Занятие 21. Kafka Connect. Простые коннекторы

Три живых включения: LIVE REST (слайд 20), Демо 1 (слайд 22), Демо 2 (слайд 39).
Стенд поднимается заранее по `../actions.md`. Команды выполняются из каталога `kafka-connect/`.

Все команды прогнаны 18.08.2026, работают.

---

## Демо 1 — слайд 22. Connect поднялся

`1.` Стенд уже поднят, показываем состав:
```shell
docker compose ps
```

`2.` Connect отвечает — версия, коммит, идентификатор кластера Kafka:
```shell
curl http://localhost:8083
```

Ответ:
```json
{"version":"7.6.1-ccs","commit":"11e81ad2a49d","kafka_cluster_id":"dt2ihMJnRK6ehfqqDTwt7g"}
```

`3.` Служебные темы Connect созданы сами при старте — те самые `config.storage.topic`,
`offset.storage.topic`, `status.storage.topic` со слайда 18:
```shell
docker exec kafka1 kafka-topics --list --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094
```

---

## LIVE REST — слайд 20. Ручки REST API

`4.` Какие плагины поставлены на исполнителя:
```shell
curl http://localhost:8083/connector-plugins | jq
```

Отсюда видно ровно то, что обсуждали на слайде 18: JDBC Source и Sink, Debezium,
файловые коннекторы, зеркалирование.

`5.` Список активных коннекторов. Сейчас пусто, к концу занятия здесь будет два:
```shell
curl http://localhost:8083/connectors | jq
```

`6.` Ручки `pause` и `resume` со слайда 19 показываем в конце Демо 2, на живом коннекторе:
сейчас останавливать нечего.

---

## Демо 2 — слайд 39. Файл в топик и обратно в файл

`7.` Создаём тему для данных:
```shell
docker exec kafka1 kafka-topics --create --topic data \
  --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094
```

`8.` Что лежит в источнике:
```shell
cat data/source.csv
```

`9.` Поднимаем файловый источник:
```shell
curl -X POST --data-binary "@webinar-21/connectors/source.json" \
  -H "Content-Type: application/json" http://localhost:8083/connectors | jq
```

`10.` Коннектор и его задача в состоянии RUNNING:
```shell
curl -s http://localhost:8083/connectors/load/status | jq -c '[.name, .connector.state, (.tasks[].state)]'
```

`11.` Данные уехали в топик. Виден JSON с полями `schema` и `payload` — это работа
преобразователя формата, о котором говорили на слайдах 32 и 33:
```shell
docker exec kafka1 kafka-console-consumer --topic data \
  --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094 --from-beginning --max-messages 5
```

`12.` Поднимаем файловый приёмник:
```shell
curl -X POST --data-binary "@webinar-21/connectors/sink.json" \
  -H "Content-Type: application/json" http://localhost:8083/connectors | jq
```

`13.` Конвейер замкнулся, в приёмнике те же строки:
```shell
docker exec connect cat /data/dump.csv
```

`14.` Главный кадр демо: дописываем строку в источник и видим её на выходе через несколько секунд.
Конвейер работает потоково, перезапускать ничего не нужно:
```shell
echo "6,Sergey,Proshchaev,3500.00" >> data/source.csv
sleep 5
docker exec connect cat /data/dump.csv
```

`15.` Оба коннектора живы:
```shell
curl -s http://localhost:8083/connectors | jq
```

`16.` Ручки `pause` и `resume` со слайда 19 — на живом коннекторе. Ставим источник на паузу,
дописываем строку в файл, показываем, что на выходе её нет, снимаем паузу — строка доезжает:
```shell
curl -s -X PUT http://localhost:8083/connectors/load/pause
curl -s http://localhost:8083/connectors/load/status | jq -c '[.connector.state, (.tasks[].state)]'

echo "7,Pause,Test,4000.00" >> data/source.csv
sleep 5
docker exec connect cat /data/dump.csv | tail -2

curl -s -X PUT http://localhost:8083/connectors/load/resume
sleep 8
docker exec connect cat /data/dump.csv | tail -2
```

---

## Если нужно прогнать демо заново

Коннекторы хранят смещение по имени, поэтому повторный запуск с тем же именем продолжит
с прежнего места. Полный сброс:

```shell
curl -s -X DELETE http://localhost:8083/connectors/load
curl -s -X DELETE http://localhost:8083/connectors/dump
docker exec kafka1 kafka-consumer-groups --bootstrap-server kafka1:19092 --delete --group connect-dump
docker exec kafka1 kafka-topics --delete --topic data --bootstrap-server kafka1:19092
docker exec connect rm -f /data/dump.csv
git checkout data/source.csv
```
