package com.sabbreview.model;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(name="authenticated-delete", query = "delete from applications where id = :id and applicant = :principle")
})
@Entity(name = "applications") @UuidGenerator(name = "APPLICATION_ID_GEN") public class Application
 extends Model {

  @Id
  @GeneratedValue(generator="APPLICATION_ID_GEN")
  private String id;

  @ManyToOne() private User applicant;

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

  @Override public String toString() {
    return "Application{" + "id='" + id + '\'' + ", applicant=" + applicant + '}';
  }
}
