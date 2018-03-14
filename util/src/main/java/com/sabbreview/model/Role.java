package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(name="get-all-roles", query = "select a.name from roles a"),
})
@Entity(name = "roles") public class Role extends Model {
  @Id
  private String name;

  private boolean canChangeApplicationState;
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

  public boolean can() {
    return canChangeApplicationState;
  }

  public Role setCanChangeApplicationState(boolean canRefuseApplication) {
    this.canChangeApplicationState = canRefuseApplication;
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
    return "Role{" + "name='" + name + '\'' + ", canRefuseApplication=" + canChangeApplicationState
        + ", canComment=" + canComment + ", canEdit=" + canEdit + '}';
  }
}
