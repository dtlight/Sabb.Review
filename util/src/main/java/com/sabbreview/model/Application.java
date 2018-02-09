package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({
    @NamedQuery(name="authenticated-delete", query = "delete from Application a where a.id = :id"),
    @NamedQuery(name="get-all-for-user", query = "select a from Application a where a.applicant.emailAddress = :owner"),
    @NamedQuery(name="get-all-for-department", query = "select a from Application a where a.department.id = :id")

})
@Entity(name = "applications")
public class Application
 extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  @ManyToOne() User applicant = null;

  @OneToMany(cascade = CascadeType.PERSIST) List<FieldInstance> fields = new ArrayList<>();

  @OneToMany(cascade = CascadeType.PERSIST) List<Assignment> assignments = new ArrayList<>();

  @ManyToOne() Department department = null;

  @Enumerated()
  private AcceptanceState state;

  public Application() {

  }

  public Application(User applicant) {
    this.setApplicant(applicant);
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
    if(applicant.applications == null) {
      applicant.applications = new ArrayList<>();
    }
    applicant.applications.add(this);
  }


  public Application setState(AcceptanceState state) {
    this.state = state;
    return this;
  }

  public AcceptanceState getState() {
    return state;
  }


  public Application addFieldInstance(FieldInstance fieldInstance) {
    if(this.fields == null) this.fields = new ArrayList<>();
    this.fields.add(fieldInstance);
    return this;
  }

  public Application setDepartment(Department department) {
    this.department = department;
    department.applications.add(this);
    return this;
  }

  public Department getDepartment() {
    return department;
  }

  public List<FieldInstance> getFields() {
    return fields;
  }

  public List<Assignment> getAssignments() {
    return assignments;
  }

  public Application addAssignment(Assignment assignment) {
    this.assignments.add(assignment);
    assignment.application = this;
    return this;
  }


  @Override public String toString() {
    return "Application{" + "id='" + id + '\'' + ", applicant=" + applicant + ", fields=" + fields
        + ", department=" + department + ", state=" + state + '}';
  }


}


