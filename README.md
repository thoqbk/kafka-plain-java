# Kafka plain java

With 5 demos, this article helps readers to understand fundamental concepts in Kafka

## Kafka terminology

There are some key terms using in Kafka:
- Topic: Kafka messages are organized into topics. To consume messages, consumers need to specify the topic they want to read from. Same for producers when writing messages
- Partition: each topic has 1 or more paritions. Based on the key of the message, Kafka decides which partition the message is located. Also, with proper configuration, Kafka can replicate partitions to multiple machines to improve read performance and data recovery ability

## Prerequisites
- JDK 11+
- Follow [step 1 and 2](https://kafka.apache.org/quickstart) to install and test Kafka on your local
- Install [Docker](https://www.docker.com/get-started/) on your local. Lately we will use this to setup a Kafka cluster on Docker which is convenient for testing
- Clone the repository [kafka-plain-java](https://github.com/thoqbk/kafka-plain-java) to prepare for experiments

There are some important files in this repository:
- demo.jar: runnable jar file. Some commands we will use in demos:

| Command | Description |
| - | - |
| java -jar demo.jar --mode c --id "Consumer 2" | to start a consumer with id `Consumer 2`. Using `--mode p` to start a producer |
| java -jar demo.jar --mode c --topic topic-demo3 --bootstrap-server localhost:29092 | to start a consumer, connect to `localhost:29092` and consume messages in topic `topic-demo3` |

- RunnableConsumer.java: to consume messages when running with `--mode c`
- RunnableProducer.java: to produce messages when running with `--mode p`

## Demos:
- [Demo 1: multi-consumer instances with 1 topic and multi-partition](docs/demo-1-multi-partition.md): to understand how Kafka messsages are distributed to partition and how they're consumed if there're multiple consumers
- [Demo 2: Kafka cluster](docs/demo-2-kafka-cluster.md): to answer 2 questions: 1/ how to setup a Kafka cluster locally? 2/ in a cluster, producer sends messages to 1 broker, can consumers read these messages from another one?
- [Demo 3: testing replication](docs/demo-3-testing-replication.md): to test the partition replication in a Kafka cluster
- [Demo 4: Kafka cluster in Docker](docs/demo-4-kafka-cluster-in-docker.md)
- [Demo 5: data persistence](docs/demo-5-data-persistence.md)

## References
- [Apache Kafka Tutorial](https://www.tutorialspoint.com/apache_kafka/index.htm)
- [What is Apache Kafka?](https://www.youtube.com/watch?v=FKgi3n-FyNU)
- [Introduction](https://kafka.apache.org/intro)
- [Message Delivery Semantics](https://kafka.apache.org/documentation/#semantics)