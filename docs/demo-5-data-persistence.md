# Demo 5: data persistence

## Start Kafka brokers

The following steps will start 2 Kafka brokers which persist data to `/tmp/docker-kafka` folder

1. Create folders for data persistence
```shell
cd /tmp
mkdir docker-kafka
mkdir docker-kafka/kafka docker-kafka/kafka2 docker-kafka/zookeeper
chmod -R 777 /tmp/docker-kafka
```

2. Start Kafka brokers
```shell
cd config
docker-compose -f docker-compose-persistence.yml up
```

3. Create new topic
```shell
bin/kafka-topics.sh --create --topic topic-demo-4 --partitions 3 --replication-factor 2 --bootstrap-server localhost:39093
```

4. Send some messages to the topic
```shell
java -jar demo.jar --mode p --messages 10 --topic topic-demo-4 --bootstrap-server localhost:39093
```

## Test data persistence

We will take down the cluster, bring it up, check to ensure the messages we sent at the previous steps are still there.

1. Shutdown the cluster

`Ctr+C` to shutdown the cluster. Remove resources:
```shell
docker-compose -f docker-compose-persistence.yml down
```

2. Start new cluster
```shell
docker-compose -f docker-compose-persistence.yml up
```

3. Consume messages
```shell
java -jar demo.jar --mode c --topic topic-demo3 --bootstrap-server localhost:29092
```

Verify the log:
```
...
08:04:07.982 INFO  - Received key 0, value This is record 0, partition 2, offset 0
08:04:07.982 INFO  - Consumer 9a9823c1-05df-4cad-b10c-5e39c7ff6cdf consumed 1 message(s)
08:04:08.002 INFO  - Consumer is polling for new messages
08:04:08.003 INFO  - Received key 7, value This is record 7, partition 2, offset 1
...
```

Notes:
- The cluster does not start successfully sometimes (see `shutting down` message in the log). If that happens, let `Ctr+C`, wait and start again
- `chmod 777` in the previous step is not safe. Did try [this](https://techoverflow.net/2018/12/15/solving-bitnami-docker-redmine-cannot-create-directory-bitnami-mariadb-permission-denied/) but still got access denied when brokers try to create new folder in the mounted volumes.