package com.sabbreview.controller;

import com.sabbreview.model.Application;
import com.sabbreview.model.Department;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import java.util.List;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class DepartmentController extends Controller {

    private static TransactionState<Department> createDepartment(String principle, Department department){
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

    private static TransactionState<Department> updateDepartment(String principle, Department department){
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


    private static TransactionState<Department> getDepartment(String principle, String depID) {
        try {
            Department department = em.find(Department.class, depID);
            return new TransactionState<>(department, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            e.printStackTrace();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    private static TransactionState<List<Department>> getDepartments(String principle) {
        try {
            List<Department>
                departments = em.createNamedQuery("get_all_departments", Department.class).getResultList();

            return new TransactionState<>(departments, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    private static TransactionState<Department> deleteDepartment(String principle, String depID) {
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


    private static TransactionState<List<Application>> getApplications(String principle, String depID) {
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

    public static void attach() {
        delete("/department/:depID", (req, res) -> requireAuthentication(req,
                (principle) -> toJson(DepartmentController.deleteDepartment(principle, req.params("depID")))));

        get("/department/:depID", (req, res) -> requireAuthentication(req,
            (principle) -> toJson(DepartmentController.getDepartment(principle, req.params("depID")))));


        get("/departments", (req, res) -> requireAuthentication(req,
            (principle) -> toJson(DepartmentController.getDepartments(principle))));

        get("/department/:id/applications", (req, res) -> requireAuthentication(req,
            (principle) -> toJson(DepartmentController.getApplications(principle, req.params("id")))));

        post("/department", (req, res) -> requireAuthentication(req, (principle) -> toJson(
                DepartmentController.createDepartment(principle, fromJson(req.body(), Department.class)))));

        put("/department", (req, res) -> requireAuthentication(req, (principle) -> toJson(
            DepartmentController.updateDepartment(principle, fromJson(req.body(), Department.class)))));

        /*get("/department/:id/templates/", (req, res) -> requireAuthentication(req, (principle) -> toJson(
            DepartmentController.updateDepartment(principle, fromJson(req.body(), DepartmentAdapter.class)))));
        */

    }
}
