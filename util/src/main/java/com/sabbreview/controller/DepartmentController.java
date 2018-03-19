package com.sabbreview.controller;

import com.sabbreview.model.Application;
import com.sabbreview.model.Department;
import com.sabbreview.model.Template;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import java.util.List;



public class DepartmentController extends Controller {

    /**
     * Creates a new department in the database.
     * Only admins can create departments.
     * @param principle User creating the department.
     * @param department Department name to create
     */
    public static TransactionState<Department> createDepartment(String principle,
        Department department){

        User user = em.find( User.class, principle);

        try {
            if( user.getAdmin()) {
                if (department == null) {
                    department = new Department();
                }
                em.getTransaction().begin();
                User HOD = em.find(User.class, principle);
                department.setHOD(HOD);
                em.persist(department);
                em.getTransaction().commit();
                return new TransactionState<>(department, TransactionStatus.STATUS_OK, "");
            }

            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "BAD PERMISSIONS");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Changes a department (in some way).
     * Merges the given department with the persistent department.
     * Principle must be admin to run.
     * @param principle User changing a department.
     * @param department Department object to change. ID must already exist in the database.
     */
    public static TransactionState<Department> updateDepartment(String principle,
        Department department){

        User user = em.find( User.class, principle);

        if( user.getAdmin()) {
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

        return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "BAD PERMISSIONS");
    }

    /**
     * Gets a department object.
     * @param principle Principle of the calling user.
     * @param depID  Department to fetch.
     * @return A department object. (as a transactionstate)
      */
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
            List<Department>
                departments = em.createNamedQuery("get_all_departments", Department.class).getResultList();

            return new TransactionState<>(departments, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Deletes a department.
     * Caller must be an admin to delete a department.
     * @param principle Principle of the calling user.
     * @param depID Department to delete.
     */
    public static TransactionState<Department> deleteDepartment(String principle, String depID) {
        try {
            User user = em.find( User.class, principle);
            if(user.getAdmin()) {
                em.getTransaction().begin();
                Department department = em.find(Department.class, depID);
                em.remove(department);
                em.getTransaction().commit();
                return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
            }

            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "BAD PERMISSIONS");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }


    /**
     * Gets the applications for a department.
     * @param principle Principle of the calling user.
     * @param depID Department to fetch applications for.
     * @return A list of applications (in a transactionstate).
     */
    public static TransactionState<List<Application>> getApplications(String principle,
        String depID) {
        try {
            em.getTransaction().begin();
            List<Application> applicationList = em.createNamedQuery("get-all-for-department", Application.class).setParameter("id", depID).getResultList();
            em.getTransaction().commit();
            return new TransactionState<>(applicationList, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Gets the templates for a department.
     * @param principle Principle of the calling user.
     * @param depID Department to fetch templates for.
     * @return A list of templates.
     */
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
