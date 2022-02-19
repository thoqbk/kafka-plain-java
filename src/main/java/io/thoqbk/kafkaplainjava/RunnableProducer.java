package io.thoqbk.kafkaplainjava;

import io.thoqbk.kafkaplainjava.config.Config;
import io.thoqbk.kafkaplainjava.exception.EnqueueException;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class RunnableProducer implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(RunnableProducer.class);

  private int messages;
  private String id;

  public RunnableProducer(String id, int messages) {
    this.messages = messages;
    this.id = id;
  }

  @Override
  public void run() {
    Producer<Long, String> producer = createProducer(id);
    for (long idx = 0; idx < messages; idx++) {
      ProducerRecord<Long, String> record =
          new ProducerRecord<>(Config.TOPIC_NAME, idx, "This is record " + idx);
      RecordMetadata metadata = send(producer, record);
      logger.info(
          "Record sent with key {} to partition {} with offset {}",
          idx,
          metadata.partition(),
          metadata.offset());
    }
  }

  private Producer<Long, String> createProducer(String id) {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KAFKA_BROKERS);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, id);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    return new KafkaProducer<>(props);
  }

  private RecordMetadata send(
      Producer<Long, String> producer, ProducerRecord<Long, String> record) {
    try {
      return producer.send(record).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new EnqueueException(e);
    }
  }
}
