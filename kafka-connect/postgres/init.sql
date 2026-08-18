-- Стенд занятий 21 и 22. Выполняется один раз при первом старте контейнера postgres.
--
-- Схема public  — таблица clients для Демо 3 и Демо 5 (JDBC Source, SMT).
--                 Таблицу customers создаёт сам JDBC Sink в Демо 4 (auto.create=true),
--                 поэтому здесь её нет: это часть демонстрации.
-- Схема inventory — для Демо 6 (Debezium, CDC).

-- ---------------------------------------------------------------- clients

CREATE TABLE clients (
    id            int PRIMARY KEY,
    first_name    text,
    last_name     text,
    gender        text,
    card_number   text,
    bill          numeric(7,2),
    created_date  timestamp NOT NULL DEFAULT current_timestamp(0),
    modified_date timestamp NOT NULL DEFAULT current_timestamp(0)
);

-- 300 строк: на слайде Демо 3 идёт UPDATE ... WHERE id = 262, строка должна существовать.
-- Значения детерминированные, чтобы демо повторялось одинаково.
INSERT INTO clients (id, first_name, last_name, gender, card_number, bill, created_date, modified_date)
SELECT
    g,
    'Ivan_'  || g,
    'Petrov_' || g,
    CASE WHEN g % 2 = 0 THEN 'M' ELSE 'F' END,
    '4276' || lpad(g::text, 12, '0'),
    (1000 + g * 7)::numeric(7,2),
    timestamp '2026-01-01 10:00:00' + (g || ' minutes')::interval,
    timestamp '2026-01-01 10:00:00' + (g || ' minutes')::interval
FROM generate_series(1, 300) AS g;

-- ---------------------------------------------------------------- inventory (CDC)

CREATE SCHEMA inventory;

CREATE TABLE inventory.customers (
    id         int PRIMARY KEY,
    first_name text NOT NULL,
    last_name  text NOT NULL,
    email      text NOT NULL UNIQUE
);

-- Те же строки, что в примере Debezium: на слайде Демо 6
-- идёт UPDATE customers SET first_name = 'Sarah' WHERE id = 1001
INSERT INTO inventory.customers (id, first_name, last_name, email) VALUES
    (1001, 'Sally',  'Thomas',  'sally.thomas@acme.com'),
    (1002, 'George', 'Bailey',  'gbailey@foobar.com'),
    (1003, 'Edward', 'Walker',  'ed@walker.com'),
    (1004, 'Anne',   'Kretchmar', 'annek@noanswer.org');

-- По умолчанию PostgreSQL кладёт в журнал только первичный ключ, и в событии CDC
-- поле before приходит пустым. FULL заставляет писать строку целиком —
-- на демо видно и «что было», и «что стало».
ALTER TABLE inventory.customers REPLICA IDENTITY FULL;

-- Debezium публикует изменения через pgoutput, отдельного плагина не нужно.
-- wal_level=logical выставлен в docker-compose.yaml командой запуска postgres.
