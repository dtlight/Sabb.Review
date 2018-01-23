package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(name="authenticated-delete", query = "delete from applications a where a.id = :id")
})
@Entity(name = "applications")
public class Application
 extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  @ManyToOne() private User applicant = null;

  @Enumerated
  private AcceptanceState state = AcceptanceState.PENDING;

  public Application() {

  }

  public Application(User applicant) {
    this.applicant = applicant;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;

  }

  public User getApplicant() {
    return applicant;
  }

  public void setApplicant(User applicant) {
    this.applicant = applicant;
  }


  public Application setState(AcceptanceState state) {
    this.state = state;
    return this;
  }

  public AcceptanceState getState() {
    return state;
  }

  @Override public String toString() {
    return "Application{" + "id='" + id + '\'' + ", applicant=" + applicant + ", state=" + state
        + '}';
  }
}


