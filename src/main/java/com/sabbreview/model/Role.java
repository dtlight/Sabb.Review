package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "roles")
public class Role {
  @Id
  @SequenceGenerator(initialValue = 1, name = "role_id_gen")
  int id;
  public String name;

  public Role() {}

  public Role(String name) {
    this.name = name;
  }
}
