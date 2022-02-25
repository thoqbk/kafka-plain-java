# Demo 4: Kafka cluster in docker
To know:
- how to run a Kafka cluster in Docker

Prerequisites:
- Docker

1. Go to `config` folder, start the cluster by running
```shell
docker-compose up --remove-orphans
```

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

## References
- [Kafka Listeners – Explained](https://www.confluent.io/blog/kafka-listeners-explained/)