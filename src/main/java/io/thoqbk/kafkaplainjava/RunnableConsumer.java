package io.thoqbk.kafkaplainjava;

import io.thoqbk.kafkaplainjava.config.Config;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class RunnableConsumer implements Runnable {
  private String id;

  public RunnableConsumer(String id) {
    this.id = id;
  }

  @Override
  public void run() {
    Consumer<Long, String> consumer = createConsumer(id);

    int noMessageFound = 0;
    while (true) {
      ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
      if (consumerRecords.isEmpty()) {
        noMessageFound++;
        if (noMessageFound > Config.MAX_NO_MESSAGE_FOUND_COUNT) {
          break;
        }
        continue;
      }

      consumerRecords.forEach(
          record -> {
            System.out.println("Key: " + record.key());
            System.out.println("Value: " + record.value());
            System.out.println("Partition: " + record.partition());
            System.out.println("Offset: " + record.offset());
          });
      consumer.commitSync();
    }
  }

  private Consumer<Long, String> createConsumer(String id) {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KAFKA_BROKERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, Config.GROUP_ID_CONFIG);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, Config.MAX_POLL_RECORDS);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Config.OFFSET_RESET_EARLIEST);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, id);

    Consumer<Long, String> retVal = new KafkaConsumer<Long, String>(props);
    retVal.subscribe(Collections.singletonList(Config.TOPIC_NAME));

    return retVal;
  }
}
