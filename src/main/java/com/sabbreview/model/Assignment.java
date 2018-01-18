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

  Date dueDate;

  public Assignment() {
  }

  public Assignment(User owner, Application application, Role role) {
    this.owner = owner;
    this.application = application;
    this.role = role;
  }


  @Override public String toString() {
    return "Assignment{" + "id=" + id + ", owner=" + owner + ", application=" + application
        + ", role=" + role + ", dueDate=" + dueDate + '}';
  }
}
