package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "users")
public class User {
  @Id
  String emailAddress;
  String password;
  public Boolean isAdmin;

  @OneToMany
  public List<Assignment> assignments = new ArrayList<>();

  public User() {}

  public User(String emailAddress, String password) {
    this.setEmailAddress(emailAddress);
    this.setPassword(password);
  }
  @Override public String toString() {
    return "User("+emailAddress+")";
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void addAssignment(Assignment assignment) {
    this.assignments.add(assignment);
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public User setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }
}
