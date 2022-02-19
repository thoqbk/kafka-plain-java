package io.thoqbk.kafkaplainjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.ExecutionException;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    int exitCode = new CommandLine(new Command()).execute(args);
    logger.info("Stopped with exitCode {}", exitCode);
  }
}
