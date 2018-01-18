package com.sabbreview.model;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "applications") @UuidGenerator(name = "APPLICATION_ID_GEN") public class Application
    extends Model {

  @Id
  @GeneratedValue(generator="APPLICATION_ID_GEN")
  public String id;

  @ManyToOne private transient User applicant;

  public Application() {

  }

  public Application(User applicant) {
    this.applicant = applicant;
  }

  public String getId() {
    return id;
  }

  public Application setId(String id) {
    this.id = id;
    return this;
  }

  public User getApplicant() {
    return applicant;
  }

  public Application setApplicant(User applicant) {
    this.applicant = applicant;
    return this;
  }

  @Override public String toString() {
    return "Application{" + "id='" + id + '\'' + ", applicant=" + applicant + '}';
  }
}
