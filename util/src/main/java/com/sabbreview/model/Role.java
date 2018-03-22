package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(name="get-all-roles", query = "select a.id, a.name from roles a"),
})

/*
 * Model class for the JPA entity manager to store role database entries in.
 */
@Entity(name = "roles") public class Role extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

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

  public String getId() {
    return id;
  }

  public Role setId(String id) {
    this.id = id;
    return this;
  }

  public boolean isCanChangeApplicationState() {
    return canChangeApplicationState;
  }

  @Override public String toString() {
    return "Role{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", canChangeApplicationState="
        + canChangeApplicationState + ", canComment=" + canComment + ", canEdit=" + canEdit + '}';
  }
}
