package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "roles") public class Role extends Model {
  @Id
  private String name;

  private boolean canRefuseApplication;
  private boolean canComment;
  private boolean canEdit;

  public Role() {}

  public Role(String name) {
    this.name = name;
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
    return "Role{" + "name='" + name + '\'' + ", canRefuseApplication=" + canRefuseApplication
        + ", canComment=" + canComment + ", canEdit=" + canEdit + '}';
  }
}
