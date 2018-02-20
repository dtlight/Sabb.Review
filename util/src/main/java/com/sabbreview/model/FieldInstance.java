package com.sabbreview.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class FieldInstance extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  @ManyToOne
  Field field;

  String value; // Only for text (not date/multichoice etc...)

  @ManyToOne(cascade = CascadeType.PERSIST)
  FieldOption option;

  public FieldInstance() {

  }

  public FieldInstance(Field field) {
    setField(field);
  }

  public String getId() {
    return id;
  }

  public FieldInstance setId(String id) {
    this.id = id;
    return this;
  }

  public Field getField() {
    return field;
  }

  public FieldInstance setField(Field field) {
    this.field = field;
    return this;
  }

  public String getValue() {
    return value;
  }

  public FieldInstance setValue(String value) {
    this.value = value;
    return this;
  }

  public FieldOption getOption() {
    return option;
  }

  public FieldInstance setOption(FieldOption option) {
    this.option = option;
    return this;
  }

  @Override public String toString() {
    return "FieldInstance{" + "id='" + id + '\'' + ", field=" + field + ", value='" + value + '\''
        + ", option=" + option + '}';
  }
}
