package com.sabbreview.model;


import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

/*
 * Model class for the JPA entity manager to store fieldOption database entries in.
 */
@Entity public class FieldOption extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;

  String title;

  Date createdAt;

  public FieldOption() {}

  public String getTitle() {
    return title;
  }

  public FieldOption setTitle(String title) {
    this.title = title;
    return this;
  }

  public int getId() {
    return id;
  }

  public FieldOption setId(int id) {
    this.id = id;
    return this;
  }

  @Override public String toString() {
    return "FieldOption{" + "id=" + id + ", title='" + title + '\''
        + '}';
  }

  @PrePersist
  protected void onCreate() {
    createdAt = new Date();
  }

}
