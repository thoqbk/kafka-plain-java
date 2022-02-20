package io.thoqbk.kafkaplainjava;

import io.thoqbk.kafkaplainjava.config.ClientConfig;
import io.thoqbk.kafkaplainjava.constant.Constants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public record RunnableConsumer(ClientConfig clientConfig, Long offset) implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(RunnableConsumer.class);

  @Override
  public void run() {
    logger.info("Consumer is starting. Id {}", clientConfig.getId());
    Consumer<Long, String> consumer = createConsumer();
    if (offset != null) {
      seekOffset(consumer);
    }
    int noMessageFound = 0;
    int consumedMessages = 0;
    while (true) {
      logger.info("Consumer is polling for new messages");
      ConsumerRecords<Long, String> consumerRecords =
              consumer.poll(Duration.ofSeconds(Constants.POLLING_TIME_SECOND));
      if (consumerRecords.isEmpty()) {
        noMessageFound++;
        if (noMessageFound > Constants.MAX_NO_MESSAGE_FOUND_COUNT) {
          break;
        }
        continue;
      }
      noMessageFound = 0;
      consumerRecords.forEach(
              record -> {
                logger.info(
                        "Received key {}, value {}, partition {}, offset {}",
                        record.key(),
                        record.value(),
                        record.partition(),
                        record.offset());
              });
      consumedMessages += consumerRecords.count();
      logger.info("Consumer {} consumed {} message(s)", clientConfig.getId(), consumedMessages);
      // To commit the offset of all polled messages
      // Without committing, when re-balancing happens, e.g. 1 more consumer joins, Kafka will
      // re-deliver all messages from the last commit point
      consumer.commitSync();
    }
  }

  private void seekOffset(Consumer<Long, String> consumer) {
    logger.info("Getting assignment and seeking the offset of partitions to {}", offset);
    int noAssignment = 0;
    while (noAssignment < Constants.MAX_NO_ASSIGNMENT) {
      if (consumer.assignment().isEmpty()) {
        noAssignment++;
        consumer.poll(Duration.ofSeconds(Constants.POLLING_TIME_SECOND)); // to trigger assignment
      } else {
        break;
      }
    }
    consumer.assignment().forEach(topicPartition -> {
      logger.info("Seeking offset of the topic {}, partition {} to {}", topicPartition.topic(), topicPartition.partition(), offset);
      consumer.seek(topicPartition, offset);
    });
  }

  private Consumer<Long, String> createConsumer() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clientConfig.getKafkaBrokers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, clientConfig.getConsumerGroup());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, Constants.MAX_POLL_RECORDS);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Constants.OFFSET_RESET_EARLIEST);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientConfig.getId());

    Consumer<Long, String> retVal = new KafkaConsumer<>(props);
    retVal.subscribe(Collections.singletonList(clientConfig.getTopicName()));

    return retVal;
  }
}
