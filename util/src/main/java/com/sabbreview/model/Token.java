package com.sabbreview.model;

/*
  This is not a JPA model (i.e. won't exist on DB)
 */
public class Token extends Model {
  String token;
  boolean isAdmin;

  public boolean isAdmin() {
    return isAdmin;
  }

  public Token setAdmin(boolean admin) {
    isAdmin = admin;
    return this;
  }

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

  @Override public String toString() {
    return "Token{" + "token='" + token + '\'' + ", isAdmin=" + isAdmin + '}';
  }
}
