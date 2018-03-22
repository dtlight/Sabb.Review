package com.sabbreview.controller;

import com.sabbreview.model.Application;
import com.sabbreview.model.Department;
import com.sabbreview.model.Template;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import java.util.List;


public class DepartmentController extends Controller {

    public static TransactionState<Department> createDepartment(String principle,
        Department department){
        try {
            if (department == null) {
                department = new Department();
            }
            em.getTransaction().begin();
            User HOD = em.find(User.class, principle);
            department.setHOD(HOD);
            em.persist(department);
            em.getTransaction().commit();
            return new TransactionState<>(department, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Department> updateDepartment(String principle,
        Department department){
        try {
            em.getTransaction().begin();
            em.merge(department);
            em.getTransaction().commit();
            return new TransactionState<>(department, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }


    public static TransactionState<Department> getDepartment(String principle, String depID) {
        try {
            Department department = em.find(Department.class, depID);
            return new TransactionState<>(department, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            e.printStackTrace();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<List<Department>> getDepartments(String principle) {
        try {
            User principleUser = em.find(User.class, principle);
            List<Department>
                departments = em.createNamedQuery("get_all_departments", Department.class).getResultList();

            return new TransactionState<>(departments, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Department> deleteDepartment(String principle, String depID) {
        try {
            em.getTransaction().begin();
            Department department = em.find(Department.class, depID);
            em.remove(department);
            em.getTransaction().commit();
            return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }


    public static TransactionState<List<Application>> getApplications(String principle,
        String depID) {
        try {
            em.getTransaction().begin();

            User principleUser = em.find(User.class, principle);

            List<Application> applicationList = em.createNamedQuery("get-all-for-department", Application.class)
                .setParameter("id", depID).setParameter("isAdmin", principleUser.isAdmin).setParameter("principle", principle).getResultList();
            em.getTransaction().commit();
            return new TransactionState<>(applicationList, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<List<Template>> getTemplates(String principle,
        String depID) {
        try {
            List<Template> templateList = em.createNamedQuery("get-all-templates-for-department", Template.class).setParameter("id", depID).getResultList();
            return new TransactionState<>(templateList, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

}
