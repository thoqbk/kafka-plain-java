# Demo 4: Kafka cluster in Docker
To know:
- how to run a Kafka cluster in Docker

## Prerequisites:
- Docker

## Get started
1. Go to `config` folder, start the cluster by running
```shell
docker-compose up --remove-orphans
```

The command will bring up a Kafka cluster with 1 Zookeeper instance and 2 Kafka broker instances.

2. Create new topic
```shell
bin/kafka-topics.sh --create --topic topic-demo-4 --partitions 3 --replication-factor 2 --bootstrap-server localhost:39093
```

3. Start consumer
```shell
java -jar demo.jar --mode c --topic topic-demo3 --bootstrap-server localhost:29092
```

4. Start producer connecting to different bootstrap server
```shell
java -jar demo.jar --mode p --messages 10 --topic topic-demo-4 --bootstrap-server localhost:39093
```

Check producer log:
```
...
08:48:18.083 INFO  - Record sent with key 0 to partition 2 with offset 0
08:48:18.136 INFO  - Record sent with key 1 to partition 0 with offset 0
08:48:18.162 INFO  - Record sent with key 2 to partition 0 with offset 1
...
```

Check consumer log:
```
...
08:48:34.389 INFO  - Consumer abfaab07-c89f-4013-a310-308e59fccfde consumed 8 message(s)
08:48:34.403 INFO  - Consumer is polling for new messages
08:48:34.405 INFO  - Received key 2, value This is record 2, partition 0, offset 1
08:48:34.405 INFO  - Consumer 2c302c9f-fcc0-46fd-8e38-405a5ee2d845 consumed 9 message(s)
08:48:34.418 INFO  - Consumer is polling for new messages
08:48:34.419 INFO  - Received key 4, value This is record 4, partition 0, offset 2
08:48:34.420 INFO  - Consumer ac28940d-9a19-4236-ab04-b1cb18d24d1f consumed 10 message(s)
...
```

## Understand Kafka config
```yml
# config/docker-compose.yml
  kafka:
    image: docker.io/bitnami/kafka:3
    ports:
      - "29092:29092"
      - "9092:9092"
    volumes:
      - "kafka_data:/bitnami"
    environment:
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CLIENT:PLAINTEXT,EXTERNAL:PLAINTEXT
      - KAFKA_CFG_LISTENERS=CLIENT://:9092,EXTERNAL://:29092
      - KAFKA_CFG_ADVERTISED_LISTENERS=CLIENT://kafka:9092,EXTERNAL://localhost:29092
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=CLIENT
    depends_on:
      - zookeeper
```

With this config, Docker will start 1 Kafka broker instance which listens on 2 ports:
- 9092 with name `CLIENT`
- 29092 with name `EXTERNAL`

The broker then connects with zookeeper at `zookeeper:2181` and registers its 2 addresses: `kafka:9092` and `localhost:29092`. Also, with `KAFKA_CFG_INTER_BROKER_LISTENER_NAME=CLIENT` it wants Zookeeper to tell other brokers connect to `kafka:9092` if want to talk to it

## Q & A:
1. Why we need both `KAFKA_CFG_LISTENERS` and `KAFKA_CFG_ADVERTISED_LISTENERS`?

`KAFKA_CFG_LISTENERS` tells Kafka what ports it should listen on whereas `KAFKA_CFG_ADVERTISED_LISTENERS` tells others (e.g. producers, consumers, brokers) its addresses that they can connect if want to talk to it.

They should be the same if all are running on bare metal machine (can connect using `localhost` or `127.0.0.1`) but if brokers, consumers and producers do not stay on the same machine or Docker instance, they must be diffirent. Two examples:
- Saying we have 2 kafka instances `kafka` and `kafka2`, `kafka2` for sure cannot connect to `kafka` using `localhost:29092`. It must use `kafka:9092` instead.
- Producer from host machine cannot connect to `kafka` using `kafka:9092`. It must use `localhost:29092` instead.

2. Why we need 2 listeners (`EXTERNAL` and `CLIENT`)? Can just have 1 with 1 port (e.g. `9092`)?

Kafka requires:
- 2 listeners must use 2 different ports (1)
- `KAFKA_CFG_ADVERTISED_LISTENERS` must be the subset of `KAFKA_CFG_LISTENERS` (2)

So we cannot have these kinds of config:
```yml
    environment:
      # ...
      - KAFKA_CFG_LISTENERS=CLIENT://:9092,EXTERNAL://:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=CLIENT://kafka:9092,EXTERNAL://localhost:9092
      # ...
```
or
```yml
    environment:
      # ...
      - KAFKA_CFG_LISTENERS=CLIENT://:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=CLIENT://kafka:9092,EXTERNAL://localhost:9092
      # ...
```

Since listening on 1 more port shouldn't cost must resource and the points at `(1)` and `(2)` are reasonable, it's acceptable to have 2 ports in multi-machines Kafka cluster.

## References
- [Kafka Listeners – Explained](https://www.confluent.io/blog/kafka-listeners-explained/)