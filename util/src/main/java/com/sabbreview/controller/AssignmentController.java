package com.sabbreview.controller;

import com.sabbreview.NotificationService;
import com.sabbreview.model.*;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

/**
 * Contains the high level code for operations on Assignment JPA objects.
 * Authentication is enforced here.
 * Call this class to do things with Assignments.
 * @see Assignment
 */
public class AssignmentController extends Controller {

  /**
   * Assigns an application to a user.
   * @param principle ID (email) of the user assigning the application.
   * @param applicationId ID of the application to assign.
   * @param roleId ID of the assignee's role.
   * @param assigneeId ID of the user to whom the application is being assigned.
   * @return Transaction state.
   */
  public static TransactionState<Assignment> createAssignment(String principle, String applicationId, String roleId, String assigneeId) {
    try {
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationId);
      Role role = em.find(Role.class, roleId);
      if(role == null) {
        throw new ValidationException("Must have role");
      }
      User assignee = em.find(User.class, assigneeId);
      Assignment assignment = new Assignment();
      assignment.setApplication(application);
      assignment.setAssignee(assignee);
      assignment.setRole(role);
      em.persist(assignment);
      em.getTransaction().commit();
      //new NotificationService().sendNotification(NotificationID.ASSIGNEDTO,"User", assignee.getEmailAddress());
      return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not create assignment");
    }
  }
  

  /**
   * Creates and adds a comment to an application.
   * Principle user needs to be assigned to the application (or be an admin) to comment.
   * @param principle ID (email) of the user adding the comment.
   * @param assignmentID ID of the assignment to add the comment to.
   * @param comment The string of the comment.
   * @return Transaction state.
   */
  public static TransactionState<Assignment> createComment(String principle, String assignmentID, Comment comment) {
    try {

      Assignment assignment = em.find(Assignment.class, assignmentID);
      User user = em.find( User.class, principle);

      if( assignment.getAssignee().getEmailAddress().equals(principle)|| user.getAdmin()){
        em.getTransaction().begin();
        comment.setAuthor(user);
        em.persist(comment);
        assignment.addComment(comment);
        em.persist(assignment);
        em.getTransaction().commit();
        em.getTransaction().commit();

        return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
      }

      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "BAD PERMISSIONS");
      
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not create assignment");
    }
  }

  /**
   * Deletes the assignment of an application to a user.
   * User needs to be assigned to an application or be an admin to delete an assignment.
   * @param principle ID (email) of the user deleting the assignment.
   * @param assignmentid ID of the assignment to delete.
   * @return Transaction state.
   */
  public static TransactionState<Assignment> deleteAssignment(String principle, String assignmentid) {
    try {

      User user = em.find(User.class, principle);
      Assignment assignment = em.find(Assignment.class, assignmentid);


      if(assignment == null) {
        throw new ValidationException("Assignment does not exist");
      }

      if( assignment.getAssignee().getEmailAddress().equals(principle) ||  user.getAdmin()){
        em.getTransaction().begin();
        em.remove(assignment);
        em.getTransaction().commit();
      }

      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
    } catch (ValidationException | RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Fetches an assignment.
   * @param principle ID (email) of the calling user.
   * @param assignmentId Assignment to fetch.
   * @return The assignment associated with the given ID as part of a transaction state.
   */
    public static TransactionState<Assignment> getAssignment(String principle, String assignmentId) {
      try {

        Assignment assignment = em.find(Assignment.class, assignmentId);
        User user = em.find(User.class, principle);

        if (assignment == null) {
          return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }

        if( assignment.getAssignee().getEmailAddress().equals(principle) || user.getAdmin()) {
          return new TransactionState<Assignment>(assignment, TransactionStatus.STATUS_OK);
        }

        return new TransactionState<>(assignment, TransactionStatus.STATUS_ERROR, "BAD PERMISSIONS");
      } catch (Exception e) {
        rollback();
        return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
      }
    }

}
