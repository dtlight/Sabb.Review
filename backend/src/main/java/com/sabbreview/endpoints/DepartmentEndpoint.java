package com.sabbreview.endpoints;

import com.sabbreview.controller.DepartmentController;
import com.sabbreview.model.Department;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class DepartmentEndpoint extends Endpoint {

  public static void attach() {
    delete("/department/:depID", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(DepartmentController.deleteDepartment(principle, req.params("depID")))));

    get("/department/:depID", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(DepartmentController.getDepartment(principle, req.params("depID")))));

    get("/department/:depID/templates", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(DepartmentController.getTemplates(principle, req.params("depID")))));

    get("/departments", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(DepartmentController.getDepartments(principle))));

    get("/department/:id/applications", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(DepartmentController.getApplications(principle, req.params("id")))));

    post("/department", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        DepartmentController.createDepartment(principle, fromJson(req.body(), Department.class)))));

    put("/department", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        DepartmentController.updateDepartment(principle, fromJson(req.body(), Department.class)))));
  }
}
