# kafka-for-developers
Training Course «Apache Kafka for Developers»

[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-FFFFFF??style=for-the-badge&logo=Spring)](https://spring.io/projects/spring-boot/)
[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)


  
  
<img src="https://i.postimg.cc/3xsw3Jdh/kafka-cover.png" alt="Training Course «Apache Kafka for Developers»" />
  
  
## Project structure
```
kafka-for-developers
├── .gradle
├── .idea
├── gradle
├── webinar-01
├── webinar-02
├── webinar-03
├── webinar-04
├── webinar-05
├── webinar-06
├── webinar-07
├── webinar-08
├── webinar-09 (bonus)
├── .gitignore
├── build.gradle.kts
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle.kts
```
  
  
## Docker

1. [Zookeeper](https://zookeeper.apache.org/)  
   Docker image: confluentinc/cp-zookeeper:7.6.1  
   Last pushed: 11.04.2024   
   Docker Hub: https://hub.docker.com/r/confluentinc/cp-zookeeper  
   Info: образ Docker, содержащий Apache Zookeeper, который поставляется вместе с платформой Confluent для работы с Apache Kafka.  
   
  
2. [Kafka](https://kafka.apache.org/)  
   Docker image: confluentinc/cp-kafka:7.6.1  
   Last pushed: 11.04.2024  
   Docker Hub: https://hub.docker.com/r/confluentinc/cp-kafka  
   Info: контейнер Docker, содержащий Apache Kafka, от компании Confluent. Версия образа 7.6.1 соответствует версии  
   Apache Kafka, которая включена в этот образ.  
  
  
3. [Kafdrop](https://github.com/obsidiandynamics/kafdrop)  
   Docker image: linuxforhealth/kafdrop:4.0.1  
   Last pushed: 02.11.2023  
   Docker Hub: https://hub.docker.com/r/linuxforhealth/kafdrop  
   Info: веб-интерфейс для управления и мониторинга Apache Kafka, который позволяет взаимодействовать с кластером Kafka.  
  
   
## Monitoring
  
  
1. Веб-интерфейс для мониторинга и управления Apache Kafka (Kafdrop) http://localhost:9000/
  
  
