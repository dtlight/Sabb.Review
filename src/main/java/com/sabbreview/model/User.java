package com.sabbreview.model;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "users") public class User extends Model {

  private static transient int HASH_ROUNDS = 10;

  @Id private String emailAddress;
  @SuppressWarnings("FieldCanBeLocal") private String password;
  public Boolean isAdmin;

  @OneToMany private List<Assignment> assignments = new ArrayList<>();

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

  private User setPassword(String password) {
    this.password = password;
    return this;
  }

  public void encryptPassword() {
    this.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(HASH_ROUNDS)));
  }

  private User setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }
}
