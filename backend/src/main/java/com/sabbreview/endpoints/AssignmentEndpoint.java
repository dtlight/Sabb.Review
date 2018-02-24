package com.sabbreview.endpoints;

import com.sabbreview.controller.UserController;
import com.sabbreview.model.AcceptanceState;

import static com.sabbreview.controller.AssignmentController.createAssignment;
import static com.sabbreview.controller.AssignmentController.deleteAssignment;
import static com.sabbreview.controller.AssignmentController.setAcceptanceState;
import static spark.Spark.delete;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.get;

public class AssignmentEndpoint extends Endpoint {
  public static void attach() {
    delete("/assignment/:id", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(deleteAssignment(principle, req.params(":id")))));

    post("/assignment", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createAssignment(principle, fromJson(req.body(),
            com.sabbreview.model.Assignment.class))))));

    put("/assignment/:id/state/:state", (req, res) -> toJson(setAcceptanceState(req.params(":id"),
        AcceptanceState.valueOf(req.params(":state")))));

    get("/user/assignments", (req, res) -> requireAuthentication(req,
        (principle -> toJson(UserController.getAssignmentsForUser(principle)))));
  }
}
