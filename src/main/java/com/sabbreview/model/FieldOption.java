package com.sabbreview.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity public class FieldOption extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;

  String title;
  boolean selected;

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

  public boolean isSelected() {
    return selected;
  }

  public FieldOption setSelected(boolean selected) {
    this.selected = selected;
    return this;
  }

  @Override public String toString() {
    return "FieldOption{" + "id=" + id + ", title='" + title + '\'' + ", selected=" + selected
        + '}';
  }
}
