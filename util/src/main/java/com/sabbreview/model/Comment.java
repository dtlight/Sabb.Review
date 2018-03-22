package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
/*
 * Model class for the JPA entity manager to store comment database entries in.
 */
@Entity
public class Comment extends Model {
  @GeneratedValue
  @Id
  private String id;

  @Lob
  private String body;

  @ManyToOne
  User author;

  @ManyToOne
  Assignment assignment;


  public Comment() {
  }

  public Comment(String body, User author, Assignment assignment) {
    setBody(body);
    setAuthor(author);
    setAssignment(assignment);
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

  private Comment setBody(String body) {
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

  private Comment setAssignment(Assignment assignment) {
    this.assignment = assignment;
    assignment.comments.add(this);
    return this;
  }

  @Override public String toString() {
    return "Comment{" + "id='" + id + '\'' + ", body='" + body + '\'' + ", author=" + author
        + ", assignment=" + assignment + '}';
  }
}
