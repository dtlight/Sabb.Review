package com.sabbreview.endpoints;

import com.sabbreview.controller.ApplicationController;
import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Assignment;
import com.sabbreview.controller.AssignmentController;

import static com.sabbreview.controller.ApplicationController.useTemplate;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class ApplicationEndpoint extends Endpoint {

  public static void attach() {
    delete("/application/:id", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        ApplicationController.deleteApplication(principle, req.params(":id")))));

    post("/application", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        ApplicationController
            .createApplication(principle, fromJson(req.body(), com.sabbreview.model.Application.class)))));

    get("/application/:id", (req, res) -> requireAuthentication(req,
            (principle) -> toJson(AssignmentController.getAssignmentApplication(principle, req.params(":id")))));

    post("/application/template/:templateid/department/:departmentid",
        (req, res) -> requireAuthentication(req, (principle) -> toJson(
            useTemplate(principle, req.params("templateid"), req.params("departmentid")))));


    put("/fieldinstance/:id", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        ApplicationController.changeFieldValue(principle, req.params(":id"),
            fromJson(req.body(), ApplicationController.FieldInstanceValue.class)))));

  }
}
