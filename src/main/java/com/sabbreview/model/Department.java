package com.sabbreview.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({
    @NamedQuery(name="get_all_departments", query = "SELECT d.id, d.name from departments d")
})
@Entity(name = "departments")
public class Department extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    String name;

    @ManyToOne
    User HOD;

    @OneToMany
    List<Template> templateList;


    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true )
    List<Application> applications;


    public User getHOD() {
        return HOD;
    }

    public Department setHOD(User HOD) {
        this.HOD = HOD;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Template> getTemplateList() {
        return templateList;
    }

    public Department setTemplateList(List<Template> templateList) {
        this.templateList = templateList;
        return this;
    }

    public Department addTemplate(Template template) {
        if(this.templateList == null) this.templateList = new ArrayList<>();
        this.templateList.add(template);
        return this;
    }

    public String getName() {
        return name;
    }

    public Department setName(String name) {
        this.name = name;
        return this;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public Department setApplications(List<Application> applicationList) {
        this.applications = applicationList;
        return this;
    }


    public Department addApplication(Application application) {
        if(this.applications == null) {
            this.applications = new ArrayList<>();
        }
        this.applications.add(application);
        return this;
    }

    @Override public String toString() {
        return "DepartmentAdapter{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", HOD=" + HOD
            + ", templateList=" + templateList + '}';
    }
}
