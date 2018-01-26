package com.sabbreview.controller;

import com.sabbreview.model.Assignment;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

import static spark.Spark.delete;
import static spark.Spark.post;

public class AssignmentController extends Controller {

  public static TransactionState<Assignment> createAssignment(String principle, Assignment assignment) {
    try {
      em.getTransaction().begin();
      em.persist(assignment);
      em.getTransaction().commit();
      return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not create assignment");
    }
  }

  public static TransactionState<Assignment> deleteAssignment(String principle, String assignmentid) {
    try {
      em.getTransaction().begin();
      Assignment assignment = em.find(Assignment.class, assignmentid);
      if(assignment == null) {
        throw new ValidationException("Assignment does not exist");
      } else {
        em.remove(assignment);
      }
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
    } catch (ValidationException | RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  public static void attach() {
    post("/api/assignment", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(AssignmentController.createAssignment(principle, fromJson(req.body(), Assignment.class)))));
    delete("/api/assignment/:id", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(AssignmentController.deleteAssignment(principle, req.params(":id")))));

  }

}
