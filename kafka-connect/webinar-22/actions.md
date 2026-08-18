# Занятие 22. Kafka Connect. Трансформации. Потоковый конвейер данных

Четыре живых включения: Демо 3 (слайд 14), Демо 4 (слайд 18), Демо 5 (слайд 33), Демо 6 (слайд 37).
Стенд общий с занятием 21, поднимается по `../actions.md`. Команды выполняются из каталога
`kafka-connect/`.

Все команды прогнаны 18.08.2026, работают.

---

## Демо 3 — слайд 14. База уезжает в Kafka

`1.` Что в таблице. Структура ровно со слайда 13:
```shell
docker exec postgres psql -U postgres -c "\d clients"
docker exec postgres psql -U postgres -c "SELECT count(*), max(id) FROM clients;"
```

`2.` Поднимаем JDBC Source. Режим `timestamp+incrementing` — тот самый со слайда 12:
новые строки ловим по возрастающему `id`, изменения существующих — по `modified_date`:
```shell
curl -X POST --data-binary "@webinar-22/connectors/clients.json" \
  -H "Content-Type: application/json" http://localhost:8083/connectors | jq
```

`3.` Состояние коннектора:
```shell
curl -s http://localhost:8083/connectors/clients/status | jq -c '[.name, .connector.state, (.tasks[].state)]'
```

`4.` Появилась тема с префиксом из конфигурации — `postgres.` плюс имя таблицы:
```shell
docker exec kafka1 kafka-topics --list --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094
```

`5.` Строки таблицы стали сообщениями:
```shell
docker exec kafka1 kafka-console-consumer --topic postgres.clients \
  --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094 \
  --from-beginning --max-messages 1 --property print.offset=true
```

`6.` Ключевой кадр: меняем строку в базе и видим её в топике через несколько секунд:
```shell
docker exec postgres psql -U postgres \
  -c "UPDATE clients SET bill = 5000, modified_date = current_timestamp(0) WHERE id = 262;"
```

```shell
docker exec kafka1 kafka-console-consumer --topic postgres.clients \
  --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094 --from-beginning --property print.offset=true
```

В хвосте — строка 262 с новым значением `bill`.

---

## Демо 4 — слайд 18. Kafka уезжает в базу, конвейер замыкается

`7.` Тема для входных данных:
```shell
docker exec kafka1 kafka-topics --create --topic customers \
  --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094
```

`8.` Отправляем два сообщения. Каждое несёт схему и данные: без схемы JDBC Sink
не знает, какие колонки создавать:
```shell
cat webinar-22/customers-messages.json | docker exec -i kafka1 kafka-console-producer \
  --topic customers --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094
```

`9.` Таблицы в базе ещё нет — её создаст сам коннектор, `auto.create` со слайда 16:
```shell
docker exec postgres psql -U postgres -c "SELECT * FROM public.customers;"
```

`10.` Поднимаем JDBC Sink:
```shell
curl -X POST --data-binary "@webinar-22/connectors/customers.json" \
  -H "Content-Type: application/json" http://localhost:8083/connectors | jq
```

`11.` Таблица создана, данные в ней:
```shell
docker exec postgres psql -U postgres -c "SELECT * FROM public.customers;"
```

`12.` Показать `insert.mode = upsert` в работе: отправляем сообщение с тем же `id`
и другим именем — строка не задваивается, а обновляется:
```shell
echo '{"schema":{"type":"struct","optional":false,"name":"customer","fields":[{"type":"int32","optional":false,"field":"id"},{"type":"string","optional":true,"field":"first_name"},{"type":"string","optional":true,"field":"last_name"},{"type":"string","optional":true,"field":"email"}]},"payload":{"id":1,"first_name":"Ivan","last_name":"Petrov-Vodkin","email":"ivan.petrov@example.com"}}' \
  | docker exec -i kafka1 kafka-console-producer --topic customers --bootstrap-server kafka1:19092
sleep 5
docker exec postgres psql -U postgres -c "SELECT * FROM public.customers;"
```

Здесь конвейер замкнулся: база -> Kafka -> база.

---

## Демо 5 — слайд 33. Тот же конвейер, но с трансформациями

`13.` Снимаем прежний источник и поднимаем его же с тремя SMT из списка на слайде 31:
`MaskField` прячет номер карты, `ReplaceField` выбрасывает `gender`,
`InsertHeader` добавляет заголовок с источником данных:
```shell
curl -s -X DELETE http://localhost:8083/connectors/clients
curl -X POST --data-binary "@webinar-22/connectors/clients-smt.json" \
  -H "Content-Type: application/json" http://localhost:8083/connectors | jq
```

`14.` Смотрим сообщения вместе с заголовками:
```shell
docker exec kafka1 kafka-console-consumer --topic postgres.clients \
  --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094 \
  --from-beginning --property print.headers=true --property print.offset=true
```

В хвосте видно три вещи: заголовок `source:jdbc-source-clients`, `card_number` со значением `****`
и отсутствие поля `gender` — причём его нет и в схеме сообщения.

---

## Демо 6 — слайд 37. CDC: читаем журнал транзакций

`15.` Поднимаем Debezium на схему `inventory`:
```shell
curl -X POST --data-binary "@webinar-22/connectors/inventory.json" \
  -H "Content-Type: application/json" http://localhost:8083/connectors | jq
```

`16.` Появились темы вида `dbserver1.inventory.<таблица>`:
```shell
docker exec kafka1 kafka-topics --list --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094
```

`17.` Меняем строку в базе:
```shell
docker exec -ti -e PGOPTIONS="--search_path=inventory" postgres psql -U postgres \
  -c "UPDATE customers SET first_name = 'Sarah' WHERE id = 1001;"
```

`18.` В событии видно и что было, и что стало:
```shell
docker exec kafka1 kafka-console-consumer --topic dbserver1.inventory.customers \
  --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094 --from-beginning
```

В хвосте — событие с `"op":"u"`, блоком `before` со старым именем и `after` с новым.
Разница с Демо 3: JDBC Source опрашивает таблицу и видит только итог, CDC читает журнал
транзакций и показывает сам переход. Удаление строки JDBC Source не заметит вовсе, CDC отдаст
событие с `"op":"d"`.

---

## Если нужно прогнать демо заново

```shell
for c in clients clients-smt customers inventory-connector; do
  curl -s -X DELETE http://localhost:8083/connectors/$c
done
docker exec kafka1 kafka-consumer-groups --bootstrap-server kafka1:19092 --delete --group connect-customers
docker exec postgres psql -U postgres -c "DROP TABLE IF EXISTS public.customers;"
docker exec postgres psql -U postgres -c "SELECT pg_drop_replication_slot('debezium_inventory');"
```
