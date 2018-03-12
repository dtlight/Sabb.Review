package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class FieldInstance extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  @ManyToOne
  Field field;

  String value; // Only for text (not date/multichoice etc...)

  @OneToMany(fetch = FetchType.EAGER) List<FieldOption> selected; // Only for text (not date/multichoice etc...)


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

  public List<FieldOption> getSelected() {
    return selected;
  }


  public FieldInstance setSelectedValues(ArrayList<FieldOption> newSelectedValues) {
    if(selected == null) {
      selected = new ArrayList<FieldOption>();
    }
      selected.clear();
    selected.addAll(newSelectedValues);
    return this;
  }

  @Override public String toString() {
    return "FieldInstance{" + "id='" + id + '\'' + ", field=" + field + ", value='" + value + '\'';
  }
}
