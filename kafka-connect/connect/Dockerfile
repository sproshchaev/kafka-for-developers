FROM confluentinc/cp-kafka-connect:7.6.1

# JDBC Source и Sink — Демо 3, 4, 5
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.7.6

# Debezium для PostgreSQL — Демо 6 (CDC)
RUN confluent-hub install --no-prompt debezium/debezium-connector-postgresql:2.5.4

# FileStreamSource и FileStreamSink — Демо 2.
# В образ Confluent они не входят вовсе, поэтому берём jar из Maven Central.
# Версия 3.6.1 соответствует Kafka внутри Confluent Platform 7.6.1.
RUN mkdir -p /usr/share/confluent-hub-components/filestream \
    && curl -fsSL -o /usr/share/confluent-hub-components/filestream/connect-file-3.6.1.jar \
       https://repo1.maven.org/maven2/org/apache/kafka/connect-file/3.6.1/connect-file-3.6.1.jar
