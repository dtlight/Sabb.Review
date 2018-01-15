package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;
  public String username;
  public String password;

  public User() {}

  public User(String username, String password) {
    this.setUsername(username);
    this.setPassword(password);
  }
  @Override public String toString() {
    return "User("+username+")";
  }

  public String getUsername() {
    return username;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public User setUsername(String username) {
    this.username = username;
    return this;
  }
}
