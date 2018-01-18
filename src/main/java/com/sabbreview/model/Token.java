package com.sabbreview.model;

/*
  This is not a JPA model (i.e. won't exist on DB)
 */
public class Token extends Model {
  String token;

  public Token(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public Token setToken(String token) {
    this.token = token;
    return this;
  }
}
