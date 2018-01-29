package com.sabbreview.model;

import javax.persistence.Entity;

@Entity(name = "department") public class Department extends Model {

    private User HOD;

    public User getHOD() {
        return HOD;
    }

    public Department setHOD(User HOD) {
        this.HOD = HOD;
        return this;
    }

}
