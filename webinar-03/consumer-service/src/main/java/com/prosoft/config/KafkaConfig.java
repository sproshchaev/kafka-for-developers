package com.prosoft.config;

import com.prosoft.deserializer.PersonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.util.Properties;

/**
 * Webinar-03: KafkaConfig содержит конфигурацию для консюмера в виде метода getConsumerConfig.
 * Конфигурации включают настройки для серверов Kafka, десериализации и групп потребителей.
 */
public class KafkaConfig {

    public static final String TOPIC = "topic3";
    public static final int NUM_CONSUMERS = 3;

    private static final String BOOTSTRAP_SERVERS = "localhost:9091, localhost:9092, localhost:9093";
    private static final String GROUP_ID = "my-consumer-group";

    private KafkaConfig() {
    }

    public static Properties getConsumerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Идентификатор группы потребителей (consumer group ID) */
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        /**
         * heartbeat.interval.ms определяет частоту отправки сигналов "heartbeat" (сердцебиение) от потребителя к группе
         * координации (group coordinator) для поддержания его членства в группе.
         * Значение по умолчанию составляет 3 сек. (3000 миллисекунд).
         */
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);

        /***
         * session.timeout.ms определяет максимальное время, в течение которого потребитель (consumer) должен отправить
         * координатору группы сигнал "heartbeat". Если за это время сигнал не будет получен, координатор группы считает
         * потребителя недоступным и инициирует процедуру ребалансировки.
         * Значение по умолчанию составляет 10 секунд (10000 миллисекунд).
         */
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);

        /**
         * max.poll.interval.ms определяет максимальный интервал времени между вызовами метода poll() потребителем (consumer).
         * Если этот интервал превышен, координатор группы считает, что потребитель вышел из строя, и инициирует процедуру
         * ребалансировки, назначая партиции другому потребителю в группе.
         * Значение по умолчанию составляет 5 минут (300000 миллисекунд).
         */
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);

        /**
         * partition.assignment.strategy - параметр определяющий стратегию выбора для перераспределения партиций:
         * 1) RangeAssignor (по умолчанию) - делит партиции на последовательные диапазоны и назначает их потребителям.
         * Например, если есть 3 потребителя и 6 партиций, то первый потребитель получит партиции 0 и 1,
         * второй получит 2 и 3, а третий — 4 и 5.
         * Значение "org.apache.kafka.clients.consumer.RangeAssignor".
         *
         * 2) RoundRobinAssignor - назначает партиции потребителям по круговому принципу. Например, если есть 3 потребителя
         * и 6 партиций, то каждый потребитель получит по одной партиции до тех пор, пока все партиции не будут назначены.
         * Значение: "org.apache.kafka.clients.consumer.RoundRobinAssignor".
         *
         * 3) StickyAssignor - стремится минимизировать перераспределение партиций, сохраняя предыдущие назначения там,
         * где это возможно. Это помогает уменьшить накладные расходы, связанные с перераспределением.
         * Значение: "org.apache.kafka.clients.consumer.StickyAssignor".
         *
         * 4) CooperativeStickyAssignor - похож на StickyAssignor, но поддерживает "кооперативный" режим, позволяющий
         * более плавно выполнять перераспределение партиций. Это позволяет избежать временных окон, когда некоторые партиции
         * могут остаться без владельцев в процессе ребалансировки.
         * Значение "org.apache.kafka.clients.consumer.CooperativeStickyAssignor".
         */
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        /** Включение автоматической фиксации смещений (enable.auto.commit = true).
         * По умолчанию параметр enable.auto.commit установлен в true - потребитель автоматически фиксирует оффсет после
         * обработки каждого пакета сообщений
         */
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        /**
         * max.partition.fetch.bytes определяет максимальное количество данных, которое потребитель может получить
         * с одной партиции в одном вызове fetch (в одном пакете).
         * Значение по умолчанию составляет 1 мегабайт (1048576 байт).
         */
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1048576);

        /**
         * max.poll.records - контролирует максимальное количество записей, которое потребитель может получить в одном
         * вызове метода poll(). Он позволяет ограничить количество сообщений, которое потребитель может обработать
         * за один раз, что может быть полезно для контроля нагрузки на потребителя.
         * Значение по умолчанию: 500.
         */
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

        /** Использование LongDeserializer для десериализации ключей (Key) */
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());

        /** Использование PersonDeserializer для десериализации значений (Value) */
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PersonDeserializer.class.getName());

        /** Управление поведением потребителя при первом подключении к топику или при потере сохраненного смещения:
         * - "earliest": начинает считывать сообщения с самого начала топика
         * - "latest": начинает считывать сообщения с самого последнего доступного смещения.
         * - "none": если нет сохраненного смещения, потребитель выбрасывает исключение.
         * - "error": потребитель выбрасывает исключение при отсутствии сохраненного смещения или если смещение находится
         * за пределами диапазона доступных смещений.
         * */
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }
}
