package io.thoqbk.kafkaplainjava;

import io.thoqbk.kafkaplainjava.config.Config;
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

  @Override
  public void run() {
    logger.info("Running at mode {} with id {}", mode, id);
    if (Config.PRODUCER_MODE.equals(mode)) {
      new RunnableProducer(id, messages).run();
    } else if (Config.CONSUMER_MODE.equals(mode)) {
      new RunnableConsumer(id).run();
    } else {
      throw new InvalidCommandException("Invalid mode " + mode);
    }
  }
}