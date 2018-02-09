package com.sabbreview.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@NamedQueries({
    @NamedQuery(name = "get-all-assignments-for-user", query = "select a from assignments a where a.assignee.emailAddress = :owner")
})
@Entity(name = "assignments") public class Assignment extends Model {
  @Id @SequenceGenerator(name = "ass_id_gen")
  public int id;

  @ManyToOne(targetEntity = User.class)  User assignee;

  @ManyToOne Application application;

  @ManyToOne Role role;

  @OneToMany(targetEntity = Comment.class)
  List<Comment> comments;

  @Enumerated
  private AcceptanceState state = AcceptanceState.PENDING;

  Date dueDate;

  public Assignment() {
  }

  public Assignment(User assignee, Application application, Role role) {
    setAssignee(assignee);
    setApplication(application);
    setRole(role);
  }

  public int getId() {
    return id;
  }

  public Assignment setId(int id) {
    this.id = id;
    return this;
  }

  public User getAssignee() {
    return assignee;
  }

  public Assignment setAssignee(User owner) {
    this.assignee = owner;
    owner.assignments.add(this);
    return this;
  }

  public Application getApplication() {
    return application;
  }

  public Assignment setApplication(Application application) {
    this.application = application;
    application.assignments.add(this);
    return this;
  }

  public Role getRole() {
    return role;
  }

  public Assignment setRole(Role role) {
    this.role = role;
    return this;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public Assignment setComments(List<Comment> comments) {
    this.comments = comments;
    return this;
  }

  public Assignment addComment(Comment comment) {
    if(this.comments == null) this.comments = new ArrayList<>();
    this.comments.add(comment);
    return this;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public Assignment setDueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  public AcceptanceState getState() {
    return state;
  }

  public Assignment setState(AcceptanceState state) {
    this.state = state;
    return this;
  }

  @Override public String toString() {
    return "Assignment{" + "id=" + id + ", owner=" + assignee + ", application=" + application
        + ", role=" + role + ", comments=" + comments + ", state=" + state + ", dueDate=" + dueDate + '}';
  }
}
