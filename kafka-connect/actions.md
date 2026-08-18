# Стенд: поднять, проверить, снять

Шаги демо — в `webinar-21/actions.md` и `webinar-22/actions.md`.
Все команды выполняются из каталога `kafka-connect/`.

`1.` Собрать образ Kafka Connect с плагинами. Нужно один раз, дальше образ берётся из кэша:
```shell
docker compose build
```

`2.` Поднять стенд:
```shell
docker compose up -d
```

`3.` Проверить, что всё поднялось. Connect стартует последним, ему нужно около 30 секунд:
```shell
docker compose ps
```

`4.` Проверить, что Connect отвечает:
```shell
curl http://localhost:8083
```

`5.` Проверить, что база готова:
```shell
docker exec postgres psql -U postgres -c "SELECT count(*) FROM clients;"
```

`6.` Kafdrop с топиками: http://localhost:9000

`7.` Снять коннекторы, не трогая стенд. Пригодится между прогонами:
```shell
for c in $(curl -s http://localhost:8083/connectors | tr -d '[]"' | tr ',' ' '); do
  curl -s -X DELETE http://localhost:8083/connectors/$c
done
```

`8.` Остановить стенд, сохранив данные:
```shell
docker compose stop
```

`9.` Удалить стенд целиком вместе с базой. После этого `init.sql` отработает заново:
```shell
docker compose down -v
```
