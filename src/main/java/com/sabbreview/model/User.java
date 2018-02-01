package com.sabbreview.model;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "users") public class User extends Model {
  private static transient int HASH_ROUNDS = 10;

  @Id private String emailAddress;
  @SuppressWarnings("FieldCanBeLocal") private String password;

  public Boolean isAdmin;

  @OneToMany(cascade = CascadeType.ALL) private List<Assignment> assignments = new ArrayList<>();
  @OneToMany(mappedBy="applicant", cascade = CascadeType.ALL) private List<Application> applications = new ArrayList<>();

  public User() {}

  public User(String emailAddress, String password) {
    this.setEmailAddress(emailAddress);
    this.setPassword(password);
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

  public Boolean getAdmin() {
    return isAdmin;
  }

  public User setAdmin(Boolean admin) {
    isAdmin = admin;
    return this;
  }

  public List<Assignment> getAssignments() {
    return assignments;
  }

  public User setAssignments(List<Assignment> assignments) {
    this.assignments = assignments;
    return this;
  }

  public List<Application> getApplications() {
    return applications;
  }

  public User setApplications(List<Application> applications) {
    this.applications = applications;
    return this;
  }

  public boolean verifyPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override public String toString() {
    return "User{" + "emailAddress='" + emailAddress + '\'' + ", isAdmin=" + isAdmin + '}';
  }
}
