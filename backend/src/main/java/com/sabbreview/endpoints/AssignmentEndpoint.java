package com.sabbreview.endpoints;

import com.sabbreview.controller.ApplicationController;
import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Comment;

import static com.sabbreview.controller.AssignmentController.createAssignment;
import static com.sabbreview.controller.AssignmentController.createComment;
import static com.sabbreview.controller.AssignmentController.deleteAssignment;
import static com.sabbreview.controller.AssignmentController.getAssignment;
import static com.sabbreview.controller.ApplicationController.setAcceptanceState;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class AssignmentEndpoint extends Endpoint {
  public static void attach() {
    delete("/assignment/:id", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(deleteAssignment(principle, req.params(":id")))))
    ;
    get("/assignment/:id", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(getAssignment(principle, req.params(":id")))));

    post("/assignment/application/:application/assignee/:assignee", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createAssignment(principle, req.params("application"), req.params("assignee"))))));

    put("/assignment/:id/state/:state", (req, res) -> requireAuthentication(req,
            (principle) -> toJson(setAcceptanceState(principle, req.params(":id"), req.params(":state")))));

    put("/assignment/:id/comment", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(createComment(principle, req.params(":id"), fromJson(req.body(),
            Comment.class)))));

  }
}
