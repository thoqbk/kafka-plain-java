package io.thoqbk.kafkaplainjava.exception;

public class EnqueueException extends RuntimeException {
  public EnqueueException(Throwable throwable) {
    super(throwable);
  }
}
