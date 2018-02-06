package com.sabbreview.responses;

public class TransactionState<T> {
  private T value;
  private TransactionStatus state;
  private String message;

  public TransactionState(T value, TransactionStatus state, String message) {
    this.value = value;
    this.state = state;
    this.message = message;
  }

  public TransactionState(T value, TransactionStatus state) {
    this.value = value;
    this.state = state;
  }

  public T getValue() {
    return value;
  }

  public TransactionState<T> setValue(T value) {
    this.value = value;
    return this;
  }

  public TransactionStatus getState() {
    return state;
  }

  public TransactionState<T> setState(TransactionStatus state) {
    this.state = state;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public TransactionState<T> setMessage(String message) {
    this.message = message;
    return this;
  }
}

