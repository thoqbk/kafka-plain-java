package io.thoqbk.kafkaplainjava.config;

import io.thoqbk.kafkaplainjava.constant.Constants;

import java.util.UUID;

public class ClientConfig {
  private final String id;
  private final String topicName;
  private final String kafkaBrokers;

  public ClientConfig(String id, String topicName, String kafkaBrokers) {
    this.id = id;
    this.topicName = topicName;
    this.kafkaBrokers = kafkaBrokers;
  }

  public String getId() {
    return nonEmptyOrDefault(id, UUID.randomUUID().toString());
  }

  public String getTopicName() {
    return nonEmptyOrDefault(topicName, Constants.TOPIC_NAME);
  }

  public String getKafkaBrokers() {
    return nonEmptyOrDefault(kafkaBrokers, Constants.KAFKA_BROKERS);
  }

  private String nonEmptyOrDefault(String value, String defaultValue) {
    return value == null || value.isEmpty() ? defaultValue : value;
  }
}
