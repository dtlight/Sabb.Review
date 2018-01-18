package com.sabbreview.responses;

public class AuthenticationException extends Exception {
  @Override public String getMessage() {
    return "The request could not be completed as the user is not authenticated";
  }
}
