package com.prosoft.config;

import com.prosoft.serializer.PersonSerializer;
import com.prosoft.serializer.SymbolSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;

/**
 * Webinar-02: KafkaConfig содержит конфигурацию для продюсера в виде метода getProducerConfig.
 * Конфигурации включают настройки для серверов Kafka, сериализации и групп отправителей.
 */
public class KafkaConfig {

    public static final String TOPIC_VOWELS = "vowels";
    public static final String TOPIC_CONSONANTS = "consonants";
    public static final int PARTITION = 0;


    private static final String BOOTSTRAP_SERVERS = "localhost:9091, localhost:9092, localhost:9093";

    private KafkaConfig() {
    }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Сколько узлов должны подтвердить получение записи, прежде чем считать ее успешно записанной:
         *  - acks=0: продюсер не будет ждать подтверждений от брокера
         *  - acks=1: продюсер будет ждать подтверждения от лидера партиции, но не от всех реплик
         *  - acks=all продюсер будет ждать подтверждений от всех реплик (самая надежная настройка)
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        /** Метод сжатия данных и используемые алгоритмы:
         *  - "none" - без сжатия,
         *  - "gzip" - алгоритм Gzip,
         *  - "snappy" - алгоритм Snappy (Google),
         *  - "lz4" - алгоритм LZ4;
         */
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");

        /** Использование LongSerializer для сериализации ключа (Key) */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());

        /** Использование PersonSerializer для сериализации значения (Value) */
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SymbolSerializer.class.getName());

        /** delivery.timeout.ms - максимальное время ожидания для успешной отправки сообщения.
         * Это включает время, которое сообщение находится в очереди, а также все попытки повторной отправки.
         * По умолчанию  установлено в 120000 миллисекунд (2 минуты).
         */
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 300000);

        /** batch.size - используется для определения максимального количества байтов, которые могут быть объединены
         * в одну запись перед отправкой их в брокер.
         * Значение по умолчанию составляет 16,384 байта (16 KB).
         */
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 8192);

        /** linger.ms - максимальное время в миллисекундах, которое продюсер будет ждать перед отправкой батча
         * сообщений, даже если батч еще не заполнился до batch.size.
         * Это может помочь сгруппировать больше сообщений в один запрос, тем самым улучшая производительность
         * за счет уменьшения количества отправок.
         * По умолчанию значение linger.ms равно 0 - продюсер отправляет сообщения сразу, не дожидаясь накопления сообщений в батч.
         */
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);

        /** max.block.ms - максимальное время, в течение которого вызов KafkaProducer.send() будет блокироваться,
         * если буфер, в который отправляются записи, полон.
         * Значение по умолчанию установлено в 60000 миллисекунд (1 минута).
         */
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 120000);

        /** retry.backoff.ms - время задержки между попытками повторной отправки сообщений в случае возникновения ошибок при отправке.
         * Значение по умолчанию установлено в 100 миллисекунд.
         */
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 200);

        /** request.timeout.ms - максимальное время ожидания, которое продюсер будет ждать ответа от брокера Kafka на запрос.
         * Значение по умолчанию установлено в 60 000 миллисекунд (60 секунд).
         */
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 120000);

        /** max.request.size - максимальный размер запроса, который производитель (producer) может отправить в брокер.
         * Этот параметр контролирует максимальный размер сообщения, которое может быть отправлено в одном запросе.
         * Значение по умолчанию для max.request.size составляет 1048576 байт (1 мегабайт).
         *
         * message.max.bytes - максимальный размер сообщения.
         * Значение по умолчанию для message.max.bytes составляет 1 000 000 байт (1 мегабайт).
         * Для изменения параметра message.max.bytes требуется внести изменения в конфигурацию брокера в файле конфигурации
         * в директории установки Kafka config/server.properties: message.max.bytes=1000000
         *
         * Если размер сообщения message.max.bytes превышает максимальный размер запроса max.request.size, то Kafka
         * автоматически разделит сообщение на фрагменты меньшего размера (max.message.bytes) для передачи и обработки
         * (автоматическое фрагментирование automatic message splitting).
         *
         * Установка MAX_REQUEST_SIZE_CONFIG больше, чем message.max.bytes, позволяет использовать больший размер запроса
         * для передачи данных и уменьшает вероятность проблем с превышением размера запроса в будущем.
         */
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 2097152);

        return properties;
    }

}
