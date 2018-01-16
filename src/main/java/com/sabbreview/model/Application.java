package com.sabbreview.model;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "applications")
@UuidGenerator(name="APPLICATION_ID_GEN")
public class Application {

  @Id
  @GeneratedValue(generator="APPLICATION_ID_GEN")
  public String id;

  @ManyToOne
  public transient User applicant;

  public Application() {

  }

  public Application(User applicant) {
    this.applicant = applicant;
  }

}
