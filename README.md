# Kafka plain java

Using plain java to create a simple producer and consumer

## Get started

1. Compile project by running the following command
```shell
mvn clean install assembly:single
```

the bundle file is `demo.jar` in `target` folder

2. Start kafka server

First, we need to start zookeeper
```shell
bin/zookeeper-server-start.sh config/zookeeper.properties
```

then start kafka server
```shell
bin/kafka-server-start.sh config/server.properties
```

## Demos:
- [Demo 1: multi-consumer instances with 1 topic and multi-partition](docs/demo-1-multi-partition.md)
- [Demo 2: Kafka cluster](docs/demo-2-kafka-cluster.md)
- [Demo 3: testing replication](docs/demo-3-testing-replication.md)
- [Demo 4: Kafka cluster in Docker](docs/demo-4-kafka-cluster-in-docker.md)
- [Demo 5: data persistence](docs/demo-5-data-persistence.md)

## References
- [Apache Kafka Tutorial](https://www.tutorialspoint.com/apache_kafka/index.htm)
- [What is Apache Kafka?](https://www.youtube.com/watch?v=FKgi3n-FyNU)
- [Introduction](https://kafka.apache.org/intro)
- [Message Delivery Semantics](https://kafka.apache.org/documentation/#semantics)