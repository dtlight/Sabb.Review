package com.sabbreview.controller;

import com.sabbreview.NotificationService;
import com.sabbreview.model.*;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

public class AssignmentController extends Controller {

  public static TransactionState<Assignment> createAssignment(String principle, String applicationId, String roleId, String assigneeId) {
    try {
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationId);
      Role role = em.find(Role.class, roleId);
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

  public static TransactionState<Assignment> createComment(String principle, String assignmentID, Comment comment) {
    try {
      em.getTransaction().begin();
      Assignment assignment = em.find(Assignment.class, assignmentID);
      User user = em.find(User.class, principle);
      comment.setAuthor(user);
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
      em.getTransaction().commit();
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
    } catch (ValidationException | RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }
  
    public static TransactionState<Assignment> setAcceptanceState(String applicationId, AcceptanceState acceptanceState){
        Assignment assignment;
        try{
            em.getTransaction().begin();
            assignment = em.find(Assignment.class, applicationId);
            assignment.setState(acceptanceState);
            em.getTransaction().commit();
            new NotificationService().sendNotification(NotificationID.valueOf(acceptanceState.toString()),
                    "User", assignment.getApplication().getApplicant().getEmailAddress());//need to decide on names or not
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
