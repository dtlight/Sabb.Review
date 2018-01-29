package com.sabbreview.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "department") public class Department extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    private User HOD;

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

}
