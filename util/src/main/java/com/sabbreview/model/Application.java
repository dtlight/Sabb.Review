package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name="delete-application", query = "delete from applications a " +
                "where a.id = :id"),

        @NamedQuery(name="get-application", query = "select a from applications a " +
                "where a.id = :id"),

        @NamedQuery(name="get-application-state", query = "select a.state from applications a where a.id = :id"),
    @NamedQuery(name="get-all-applications-for-user", query = "select a from applications a " +
                "where a.applicant.emailAddress = :id"),

        @NamedQuery(name="get-all-assignments-for-application", query = "select a.id, a.assignee.emailAddress, a.role.name, a.state from assignments a " +
                "where a.application.id = :id"),

        @NamedQuery(name="get-all-for-department", query = "select a from applications a " +
                "where a.department.id = :id AND ( :isAdmin = true OR a.department.HOD.emailAddress = :principle)")
})


/*
 * Model class for the JPA entity manager to store application database entries in.
 */
@Entity(name = "applications")
public class Application
        extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  @ManyToOne() User applicant = null;

  @OrderBy("field")
  @OneToMany(cascade = CascadeType.PERSIST) List<FieldInstance> fields = new ArrayList<>();

  @OneToMany(cascade = CascadeType.PERSIST) List<Assignment> assignments = new ArrayList<>();

  @ManyToOne() Department department = null;

  @Enumerated()
  private AcceptanceState state;

  @Lob
  private String signature;

  public void setSignature(String sign) {
    this.signature = sign;

  }

  public String getSignature() {
    return signature;
  }

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


