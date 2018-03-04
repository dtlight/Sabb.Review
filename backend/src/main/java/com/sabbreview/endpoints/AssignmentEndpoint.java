package com.sabbreview.endpoints;

import com.sabbreview.model.AcceptanceState;

import static com.sabbreview.controller.AssignmentController.createAssignment;
import static com.sabbreview.controller.AssignmentController.deleteAssignment;
import static com.sabbreview.controller.AssignmentController.setAcceptanceState;
import static spark.Spark.delete;
import static spark.Spark.post;
import static spark.Spark.put;

public class AssignmentEndpoint extends Endpoint {
  public static void attach() {
    delete("/assignment/:id", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(deleteAssignment(principle, req.params(":id")))));

    post("/assignment/application/:application/assignee/:assignee", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createAssignment(principle, req.params("application"), req.params("assignee"))))));

    put("/assignment/:id/state/:state", (req, res) -> toJson(setAcceptanceState(req.params(":id"),
        AcceptanceState.valueOf(req.params(":state")))));

  }
}
