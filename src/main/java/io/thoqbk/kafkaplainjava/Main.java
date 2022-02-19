package io.thoqbk.kafkaplainjava;

import picocli.CommandLine;

import java.util.concurrent.ExecutionException;

public class Main {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    int exitCode = new CommandLine(new Command()).execute(args);
    System.out.println(exitCode);
  }
}
