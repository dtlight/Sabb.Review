package com.sabbreview.model;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({
    @NamedQuery(query = "SELECT U FROM users U WHERE :isAdmin = true", name = "get-all-users")
})
@Entity(name = "users") public class User extends Model {
  private static transient int HASH_ROUNDS = 10;


  @Id private String emailAddress;
  @SuppressWarnings("FieldCanBeLocal") private String password;

  public boolean isAdmin;

  @OneToMany(cascade = CascadeType.ALL) List<Assignment> assignments = new ArrayList<>();
  @OneToMany(mappedBy="applicant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true ) List<Application> applications = new ArrayList<>();

  public User() {}

  public User(String emailAddress, String password) {
    this.setEmailAddress(emailAddress);
    this.setPassword(password);
  }

  public String getEmailAddress() {
    return emailAddress;
  }


  public void addApplication(Application application) {
    this.applications.add(application);
    application.applicant = this;
  }

  public void addAssignment(Assignment assignment) {
    this.assignments.add(assignment);
    assignment.assignee = this;
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

  public List<Application> getApplications() {
    return applications;
  }


  public boolean verifyPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override public String toString() {
    return "User{" + "emailAddress='" + emailAddress + '\'' + ", isAdmin=" + isAdmin + '}';
  }
}
