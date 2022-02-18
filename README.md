# Kafka plain java

Using plain java to create a simple producer and consumer

## Exercise 1: multi-consumer instances with 1 topic and multi-partition

To see:
- how messages distributed to partitions
- how messages distributed and consumed if having multi-consumer

Run kafka and create a topic with 2 partitions

1. Start zookeeper
```shell
bin/zookeeper-server-start.sh config/zookeeper.properties
```

2. Start kafka
```shell
bin/kafka-server-start.sh config/server.properties
```

3. (Optional) Delete topic if it already exists
```shell
bin/kafka-topics.sh --delete --topic topic-demo --bootstrap-server localhost:9092
```

4. Create topic with 2 partitions
```shell
bin/kafka-topics.sh --create --topic topic-demo --partitions 2 --bootstrap-server localhost:9092
```

Start producer and consumers

1. From Intellij, create 3 configs to run `Main.main()` function:
- `Producer` with `Program argument` `p`
- `Consumer 1` with `Program argument` `c`
- `Consumer 2` with `Program argument` `c`

2. Run `Producer` to enqueue messages
```
Record sent with key 0 to partition 1 with offset 1984
Record sent with key 1 to partition 0 with offset 2016
Record sent with key 2 to partition 0 with offset 2017
Record sent with key 3 to partition 1 with offset 1985
...
```

Observation: messages enqueued and distributed evenly among partitions

2. Run `Consumer 1`
```
Key: 1
Value: This is record 1
Partition: 0
Offset: 2016
Key: 2
Value: This is record 2
Partition: 0
Offset: 2017
...
Key: 0
Value: This is record 0
Partition: 1
Offset: 1984
Key: 3
Value: This is record 3
Partition: 1
Offset: 1985
...
```

Observation: `Consumer 1` consumes all messages from `partition 0` then `partition 1`

3. Run `Consumer 2`

Kafka re-balances consumers in the group
```
[2022-02-18 08:52:15,882] INFO [GroupCoordinator 0]: Preparing to rebalance group consumerGroup1 in state PreparingRebalance with old generation 8 (__consumer_offsets-6) (reason: Adding new member consumer-consumerGroup1-1-c56f58fd-6a92-4604-bd8f-fa51e0eec988 with group instance id None) (kafka.coordinator.group.GroupCoordinator)
```

Run producer again, `Consumer 1` consumes messages from `partition 1` and `Consumer 2` consumes messages from `partition 2` only.
