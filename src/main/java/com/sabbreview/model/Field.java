package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity public class Field extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  String title;

  FieldType type;

  @OneToMany(cascade = CascadeType.ALL)
  List<FieldOption> fieldOptions;

  public String getId() {
    return id;
  }

  public Field setId(String id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Field setTitle(String title) {
    this.title = title;
    return this;
  }

  public FieldType getType() {
    return type;
  }

  public Field setType(FieldType type) {
    this.type = type;
    return this;
  }

  public List<FieldOption> getFieldOptions() {
    return fieldOptions;
  }

  public Field setFieldOptions(List<FieldOption> fieldOptions) {
    this.fieldOptions = fieldOptions;
    return this;
  }

  public void addFieldOption(FieldOption fieldOption) {
    if(fieldOptions == null) {
      fieldOptions = new ArrayList<>();
    }
    fieldOptions.add(fieldOption);
  }

  @Override public String toString() {
    return "Field{" + "id=" + id + ", title='" + title + '\'' + ", type=" + type + ", fieldOptions="
        + fieldOptions + '}';
  }
}
