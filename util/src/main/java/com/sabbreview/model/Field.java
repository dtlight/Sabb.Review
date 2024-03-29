package com.sabbreview.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

/*
 * Model class for the JPA entity manager to store template field database entries in.
 */
@Entity public class Field extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  private String title;

  private FieldType type;

  Boolean showAtEnd = false;


  Date createdAt;

  @OneToMany(cascade = CascadeType.ALL) private List<FieldOption> fieldOptions;

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

  public java.lang.Boolean isShowAtEnd() {
    return showAtEnd;
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public Field setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  @Override public String toString() {
    return "Field{" + "id=" + id + ", title='" + title + '\'' + ", type=" + type + ", fieldOptions="
        + fieldOptions + '}';
  }

  @PrePersist
  protected void onCreate() {
    createdAt = new Date();
  }
}
