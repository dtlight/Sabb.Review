package com.sabbreview.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "assignments")
public class Assignment {
  @Id
  @SequenceGenerator(initialValue = 1, name = "ass_id_gen")
  public int id;

  @ManyToOne(targetEntity = User.class)
  public transient User owner;
  @ManyToOne
  public Application application;


  @ManyToOne
  Role role;

  Date dueDate;

  public Assignment() {
  }

  public Assignment(User owner, Application application, Role role) {
    this.owner = owner;
    this.application = application;
    this.role = role;
  }

}
