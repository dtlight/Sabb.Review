package com.sabbreview.controller;

import com.sabbreview.NotificationService;
import com.sabbreview.model.*;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

public class AssignmentController extends Controller {

  /**
   * Assigns an application to a user. Currently has no authentication.
   * @param principle Principle of the user assigning the application.
   * @param applicationId ID of the application to assign.
   * @param assigneeId ID of the user to whom the application is being assigned.
   */
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
      new NotificationService().sendNotification(NotificationID.ASSIGNEDTO,"User", assignee.getEmailAddress());
      return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not create assignment");
    }
  }

  /**
   * Creates and adds a comment to an application.
   * @param principle Principle of the user adding the comment.
   * @param assignmentID ID of the assignment to add the comment to.
   * @param comment The string of the comment.
   */
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

  /**
   * Deletes the assignment of an application to a user.
   * @param principle Principle of the user deleting the assignment.
   * @param assignmentid ID of the assignment to delete.
   */
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
