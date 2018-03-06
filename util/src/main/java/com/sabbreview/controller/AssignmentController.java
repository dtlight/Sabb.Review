package com.sabbreview.controller;

import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

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
            if (assignment == null) {
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

    /**
     * Changes the state of the application (to which the principle is an assignee) attached to an assignment
     * @param principle The user's principle (email address)
     * @param assignmentID The assignment containing the application.
     * @return A transactionState containing the acceptanceState
     */
    public static TransactionState<AcceptanceState> getAcceptanceState(String principle, String assignmentID) {
        Assignment assignment;
        try {
            em.getTransaction().begin();

            AcceptanceState state = em.createNamedQuery("get-assignment-application-acceptance-state", AcceptanceState.class)
                    .setParameter("assignmentId", assignmentID)
                    .setParameter("principle", principle)
                    .getSingleResult();

            em.getTransaction().commit();
            return new TransactionState<AcceptanceState>(state, TransactionStatus.STATUS_OK);
        } catch (RollbackException e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Changes the state of the application (to which the principle is an assignee) attached to an assignment
     * @param principle The user's principle (email address)
     * @param assignmentID The assignment containing the application.
     * @param acceptanceState The acceptanceState to set.
     */
    public static TransactionState<AcceptanceState> setAcceptanceState(String principle, String assignmentID, AcceptanceState acceptanceState) {
        Assignment assignment;
        try {
            em.getTransaction().begin();

           em.createNamedQuery("set-assignment-application-acceptance-state")
                    .setParameter("assignmentId", assignmentID)
                    .setParameter("principle", principle)
                    .setParameter("acceptanceState", acceptanceState)
                    .executeUpdate();

            em.getTransaction().commit();
            return new TransactionState<AcceptanceState>(null, TransactionStatus.STATUS_OK);
        } catch (RollbackException e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }


}
