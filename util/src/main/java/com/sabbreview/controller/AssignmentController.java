package com.sabbreview.controller;

import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.Comment;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

public class AssignmentController extends Controller {

  public static TransactionState<Assignment> createAssignment(String principle, String applicationId, String assigneeId) {
    try {
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationId);
      User assignee = em.find(User.class, assigneeId);
      Assignment assignment = new Assignment();
      assignment.setApplication(application);
      assignment.setAssignee(assignee);
      em.persist(assignment);
      em.getTransaction().commit();
      return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not create assignment");
    }
  }

  public static TransactionState<Assignment> createComment(String principle, String assignmentID, Comment comment) {
    try {
      em.getTransaction().begin();
      Assignment assignment = em.find(Assignment.class, assignmentID);
      em.persist(comment);
      assignment.addComment(comment);
      em.persist(assignment);
      em.getTransaction().commit();
      return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
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
  
    public static TransactionState<Assignment> setAcceptanceState(String applicationid, AcceptanceState acceptanceState){
        Assignment assignment;
        try{
            em.getTransaction().begin();
            assignment = em.find(Assignment.class, applicationid);
            assignment.setState(acceptanceState);
            em.getTransaction().commit();
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
        } catch (RollbackException e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Assignment> getAssignment(String principle, String assignmentId) {
      try {
        Assignment assignment;
        assignment = em.find(Assignment.class, assignmentId);
        if (assignment == null) {
          return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
        return new TransactionState<>(assignment, TransactionStatus.STATUS_OK, "");
      } catch (Exception e) {
        rollback();
        return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
      }
    }

}
