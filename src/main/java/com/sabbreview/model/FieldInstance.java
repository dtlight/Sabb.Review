package com.sabbreview.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class FieldInstance extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  int id;

  @ManyToOne
  Field field;

  String value; // Only for text (not date/multichoice etc...)

  @ManyToOne
  FieldOption option;

  @OneToMany
  List<FieldOption> options;


  public int getId() {
    return id;
  }

  public FieldInstance setId(int id) {
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

  public List<FieldOption> getOptions() {
    return options;
  }

  public FieldInstance setOptions(List<FieldOption> options) {
    this.options = options;
    return this;
  }

  @Override public String toString() {
    return "FieldInstance{" + "id=" + id + ", field=" + field + ", value='" + value + '\''
        + ", option=" + option + ", options=" + options + '}';
  }
}
