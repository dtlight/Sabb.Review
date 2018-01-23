package com.sabbreview.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity public class FieldOption extends Model {
  @Id
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

  public boolean isSelected() {
    return selected;
  }

  public FieldOption setSelected(boolean selected) {
    this.selected = selected;
    return this;
  }

  @Override public String toString() {
    return "FieldOption{" + "title='" + title + '\'' + ", selected=" + selected + '}';
  }
}
