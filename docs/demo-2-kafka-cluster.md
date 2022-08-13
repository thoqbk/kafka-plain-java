# Demo 2: Kafka cluster

To answer 2 questions:
- how to setup a Kafka cluster locally?
- in a cluster, producer sends messages to 1 broker, can consumers read these messages from another one?

## Run Kafka cluster locally

1. Make copies of `config/server.properties` to `config/server[i].properties` (number of `i` is the number of Kafka brokers in the cluster)

2. For each `config/server[i].properties` update the following fields
```
# i = 1, 2,...
broker.id={i}
# e.g. 9094
listeners=PLAINTEXT://:{new port here}
# e.g. /tmp/kafka-logs3
log.dirs=/tmp/kafka-logs{i}
```

3. Start Zookeeper

If you used Zookeeper with an old cluster before, you first need to delete `/tmp/zookeeper` or point `dataDir` in `config/zookeeper.properties` to a new folder before starting Zookeeper
```shell
bin/zookeeper-server-start.sh config/zookeeper.properties
```

4. Start Kafka brokers
```shell
bin/kafka-server-start.sh config/server1.properties
```
Use similar commands for others

5. Create topic and replicate

Can pick any host for `bootstrap-server` as the brokers all know each other
```shell
bin/kafka-topics.sh --create --topic topic-demo-2 --partitions 3 --replication-factor 2 --bootstrap-server localhost:9093
```

Check status of the topic:
```shell
bin/kafka-topics.sh --describe --topic topic-demo-2 --bootstrap-server localhost:9093
```

Result:
```
	Topic: topic-demo-2	Partition: 0	Leader: 4	Replicas: 4,1	Isr: 4,1
	Topic: topic-demo-2	Partition: 1	Leader: 1	Replicas: 1,2	Isr: 1,2
	Topic: topic-demo-2	Partition: 2	Leader: 2	Replicas: 2,4	Isr: 2,4
```

## Produce and consume messages from different brokers

1. Run consumer
```shell
java -jar demo.jar --bootstrap-server localhost:9093 --topic topic-demo-2 --mode c
```

2. Run producer
```shell
java -jar demo.jar --bootstrap-server localhost:9094 --topic topic-demo-2 --mode p --messages 10
```

3. Confirm that consumer is receiving messages
```
11:07:37.852 INFO  - Received key 3, value This is record 3, partition 1, offset 0
11:07:37.853 INFO  - Consumer a5b5c525-d73c-449b-9414-6ce551c2cc2c consumed 1 message(s)
11:07:37.875 INFO  - Consumer is polling for new messages
...
```
