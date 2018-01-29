package com.sabbreview.controller;

import com.sabbreview.model.Department;
import com.sabbreview.model.User;
import com.sabbreview.responses.AuthenticationException;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import static spark.Spark.delete;
import static spark.Spark.post;

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

    private static TransactionState<Department> deleteDepartment(String principle, String depID) {
        try {
            em.getTransaction().begin();
            Department department = em.find(Department.class, depID);
            if (department.getHOD().getDepartment().equals(principle)) {
                em.remove(department);
            } else {
                throw new AuthenticationException();
            }
            em.getTransaction().commit();
            return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static void attach() {
        delete("/department/:depID", (req, res) -> requireAuthentication(req,
                (principle) -> toJson(DepartmentController.deleteDepartment(principle, req.params("depID")))));

        post("/department", (req, res) -> requireAuthentication(req, (principle) -> toJson(
                DepartmentController.createDepartment(principle, fromJson(req.body(), Department.class)))));
    }
}
