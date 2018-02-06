package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@Entity
@NamedQueries({
    @NamedQuery( name="gettemplate", query = "SELECT t FROM Template t WHERE t.id = :id")
})
public class Template extends Model {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  boolean departmentDefault;

  String name;


  @ManyToOne
  Department department;

  @ManyToMany(fetch = FetchType.EAGER)
  public List<Field> fieldList;

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

  public boolean isDepartmentDefault() {
    return departmentDefault;
  }

  public Template setDepartmentDefault(boolean departmentDefault) {
    this.departmentDefault = departmentDefault;
    return this;
  }


  public Department getDepartment() {
    return department;
  }

  public Template setDepartment(Department department) {
    this.department = department;
    department.addTemplate(this);
    return this;
  }

  /*@Override public String toString() {
    return "Template{" + "id='" + id + '\'' + ", departmentDefault=" + departmentDefault
        + ", name='" + name + '\'' + ", department=" + department + ", fieldList=" + fieldList
        + '}';
  }*/
}
