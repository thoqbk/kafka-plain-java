package io.thoqbk.kafkaplainjava;

import io.thoqbk.kafkaplainjava.config.ClientConfig;
import io.thoqbk.kafkaplainjava.constant.Constants;
import io.thoqbk.kafkaplainjava.exception.InvalidCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(
    name = "kafka-plain-java",
    mixinStandardHelpOptions = true,
    version = "Kafka-plain-java 1.0")
public class Command implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(Command.class);

  @CommandLine.Option(
      names = {"-m", "--mode"},
      description = "Running mode, use `p` for producer, `c` for consumer")
  private String mode;

  @CommandLine.Option(
      names = {"--messages"},
      description = "Number of messages will be enqueued if running in producer mode")
  private int messages;

  @CommandLine.Option(
      names = {"--id"},
      description = "Id of the consumer or producer")
  private String id;

  @CommandLine.Option(
      names = {"--bootstrap-server"},
      description = "The Kafka server to connect to")
  private String bootstrapServer;

  @CommandLine.Option(
      names = {"--group"},
      description = "Consumer group")
  private String consumerGroup;

  @CommandLine.Option(
      names = {"--topic"},
      description = "The topic to enqueue or poll messages from")
  private String topic;

  @CommandLine.Option(
      names = {"--offset"},
      description = "Seeking partition to this offset before polling messages")
  private Long offset;

  @Override
  public void run() {
    ClientConfig config = getClientConfig();
    logger.info("Running at mode {} with id {}", mode, config.getId());
    if (Constants.PRODUCER_MODE.equals(mode)) {
      new RunnableProducer(config, messages).run();
    } else if (Constants.CONSUMER_MODE.equals(mode)) {
      new RunnableConsumer(config, offset).run();
    } else {
      throw new InvalidCommandException("Invalid mode " + mode);
    }
  }

  private ClientConfig getClientConfig() {
    return new ClientConfig(id, topic, bootstrapServer, consumerGroup);
  }
}
