package com.sabbreview.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "assignments") public class Assignment extends Model {
  @Id @SequenceGenerator(name = "ass_id_gen")
  public int id;

  @ManyToOne(targetEntity = User.class) private transient User owner;

  @ManyToOne private Application application;

  @ManyToOne private Role role;

  private String comments;

  private int acceptanceState;

  Date dueDate;

  public Assignment() {
  }

  public Assignment(User owner, Application application, Role role) {
    this.owner = owner;
    this.application = application;
    this.role = role;
  }

  public int getId() {
    return id;
  }

  public Assignment setId(int id) {
    this.id = id;
    return this;
  }

  public User getOwner() {
    return owner;
  }

  public Assignment setOwner(User owner) {
    this.owner = owner;
    return this;
  }

  public Application getApplication() {
    return application;
  }

  public Assignment setApplication(Application application) {
    this.application = application;
    return this;
  }

  public Role getRole() {
    return role;
  }

  public Assignment setRole(Role role) {
    this.role = role;
    return this;
  }

  public String getComments() {
    return comments;
  }

  public Assignment setComments(String comments) {
    this.comments = comments;
    return this;
  }

  public int getAcceptanceState() {
    return acceptanceState;
  }

  public Assignment setAcceptanceState(int acceptanceState) {
    this.acceptanceState = acceptanceState;
    return this;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public Assignment setDueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  @Override public String toString() {
    return "Assignment{" + "id=" + id + ", owner=" + owner + ", application=" + application
        + ", role=" + role + ", comments='" + comments + '\'' + ", acceptanceState="
        + acceptanceState + ", dueDate=" + dueDate + '}';
  }
}
