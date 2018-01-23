package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends Model {
  @GeneratedValue
  @Id
  private String id;

  private String body;

  @ManyToOne
  private User author;

  @ManyToOne
  private Assignment assignment;


  public Comment() {
  }

  public Comment(String body, User author, Assignment assignment) {
    this.body = body;
    this.author = author;
    this.assignment = assignment;
  }

  public String getId() {
    return id;
  }

  public Comment setId(String id) {
    this.id = id;
    return this;
  }

  public String getBody() {
    return body;
  }

  public Comment setBody(String body) {
    this.body = body;
    return this;
  }

  public User getAuthor() {
    return author;
  }

  public Comment setAuthor(User author) {
    this.author = author;
    return this;
  }

  public Assignment getAssignment() {
    return assignment;
  }

  public Comment setAssignment(Assignment assignment) {
    this.assignment = assignment;
    return this;
  }

  @Override public String toString() {
    return "Comment{" + "id='" + id + '\'' + ", body='" + body + '\'' + ", author=" + author
        + ", assignment=" + assignment + '}';
  }
}
