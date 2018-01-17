package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "roles") public class Role extends Model {
  @Id @SequenceGenerator(name = "role_id_gen")
  int id;
  private String name;

  public Role() {}

  public Role(String name) {
    this.name = name;
  }
}
