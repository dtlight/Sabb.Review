package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


@Entity
public class Template extends Model {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  String name;

  @ManyToMany
  List<Field> fieldList;

  public String getId() {
    return id;
  }

  public Template setId(String id) {
    this.id = id;
    return this;
  }

  public List<Field> getFieldList() {
    return fieldList;
  }

  public Template setFieldList(List<Field> fieldList) {
    this.fieldList = fieldList;
    return this;
  }

  public String getName() {
    return name;
  }

  public Template setName(String name) {
    this.name = name;
    return this;
  }

  public Template addField(Field field) {
    if(this.fieldList == null) this.fieldList = new ArrayList<>();
    this.fieldList.add(field);
    return this;
  }

  @Override public String toString() {
    return "Template{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", fieldList=" + fieldList
        + '}';
  }
}
