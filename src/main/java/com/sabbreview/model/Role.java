package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "roles") public class Role extends Model {
  @Id @SequenceGenerator(name = "role_id_gen")
  int id;
  private String name;

  private boolean canRefuseApplication;
  private boolean canComment;
  private boolean canEdit;

  public Role() {}

  public Role(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public Role setId(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Role setName(String name) {
    this.name = name;
    return this;
  }

  public boolean isCanRefuseApplication() {
    return canRefuseApplication;
  }

  public Role setCanRefuseApplication(boolean canRefuseApplication) {
    this.canRefuseApplication = canRefuseApplication;
    return this;
  }

  public boolean isCanComment() {
    return canComment;
  }

  public Role setCanComment(boolean canComment) {
    this.canComment = canComment;
    return this;
  }

  public boolean isCanEdit() {
    return canEdit;
  }

  public Role setCanEdit(boolean canEdit) {
    this.canEdit = canEdit;
    return this;
  }

  @Override public String toString() {
    return "Role{" + "id=" + id + ", name='" + name + '\'' + ", canRefuseApplication="
        + canRefuseApplication + ", canComment=" + canComment + ", canEdit=" + canEdit + '}';
  }
}
