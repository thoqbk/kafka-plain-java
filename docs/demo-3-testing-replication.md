# Demo 3: testing replication

To know:
- whether the cluster still works if one broker goes offline
- how to replace 1 broker by another

## Run Kafka cluster

Follow steps in [Demo 2: Kafka cluster](demo-2-kafka-cluster.md#run-kafka-cluster-locally) to run a cluster of 3 brokers (naming broker 1, 2 and 3) and create a topic with 3 `partitions` and `replication-factor` of 2

## Testing replication

1. Shutdown broker 3, there are 2 left (broker 1 and 2)
2. Run producer to generate 1000 messages
```shell
java -jar demo.jar --bootstrap-server localhost:9094 --topic topic-demo-2 --mode p --messages 1000
```

3. Run consumer, check the log and confirm that it receives 1000 messages
```shell
java -jar demo.jar --bootstrap-server localhost:9093 --topic topic-demo-2 --mode c
```

4. Start broker 3 and repeat step 2. and 3.

## Replace a broker by another

Using tool `bin/kafka-reassign-partitions.sh` to migrate data from running broker to the new one.

1. Shutdown broker 3
2. Start new broker (named broker 4)
3. Go through 3 steps `generate`, `execute` and `verify` from [here](https://kafka.apache.org/documentation/#basic_ops_cluster_expansion) to migrate data to broker 4

- Create `reassign-request.json`
```json
{"topics": [{"topic": "topic-demo-2"}], "version":1}
```
- Generate reassignment file by running the following command:
```shell
bin/kafka-reassign-partitions.sh --generate --bootstrap-server :9093 --topics-to-move-json-file reassign-request.json --broker-list 1,2,4
```
- Save the result to `reassign.json` and execute the assignment by running:
```shell
bin/kafka-reassign-partitions.sh --execute --bootstrap-server :9093 --reassignment-json-file reassign.json
```
- Verify the result
```shell
bin/kafka-reassign-partitions.sh --verify --bootstrap-server :9093 --reassignment-json-file reassign.json
```

4. Check topic status
```shell
bin/kafka-topics.sh --describe --topic topic-demo-2 --bootstrap-server localhost:9093
```

## References
- [ReassignPartitionsCommand](https://jaceklaskowski.gitbooks.io/apache-kafka/content/kafka-admin-ReassignPartitionsCommand.html)
- [Expanding your cluster](https://kafka.apache.org/documentation/#basic_ops_cluster_expansion)
