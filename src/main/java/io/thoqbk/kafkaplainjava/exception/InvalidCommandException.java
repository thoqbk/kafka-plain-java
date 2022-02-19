package io.thoqbk.kafkaplainjava.exception;

public class InvalidCommandException extends RuntimeException {
  public InvalidCommandException(String message) {
    super(message);
  }
}
