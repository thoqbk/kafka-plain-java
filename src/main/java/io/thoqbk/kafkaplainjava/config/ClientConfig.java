package io.thoqbk.kafkaplainjava.config;

import io.thoqbk.kafkaplainjava.constant.Constants;

import java.util.UUID;

public record ClientConfig(String id, String topicName, String kafkaBrokers, String consumerGroup) {

  public String getId() {
    return nonEmptyOrDefault(id, UUID.randomUUID().toString());
  }

  public String getTopicName() {
    return nonEmptyOrDefault(topicName, Constants.TOPIC_NAME);
  }

  public String getKafkaBrokers() {
    return nonEmptyOrDefault(kafkaBrokers, Constants.KAFKA_BROKERS);
  }

  public String getConsumerGroup() {
    return nonEmptyOrDefault(consumerGroup,Constants.GROUP_ID_CONFIG);
  }

  private String nonEmptyOrDefault(String value, String defaultValue) {
    return value == null || value.isEmpty() ? defaultValue : value;
  }
}
