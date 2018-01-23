package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

public class AssignmentController {
    private static EntityManager em = SabbReview.getEntityManager();


    public static TransactionState<Assignment> createApplication(String principle, Assignment assignment) {
        try {
            em.getTransaction().begin();
            em.persist(assignment); //telling jpa to store
            em.getTransaction().commit();
        } catch (RollbackException e) {

        } catch (ValidationException e) {

        }
    }
    /*public static TransactionState<Assignment> setOwner(User owner, Assignment assignment) {
        try {
            em.getTransaction().begin();
            assignment.setOwner(owner);
            em.merge(assignment);
            em.getTransaction().commit();
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Assignment> getOwner(String owner) { /////     Assignment assignment;
        try {
            Assignment assignment = em.find(Assignment.id, owner);
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Assignment> setComments(String principle, Assignment comments) { ////////// em.find to find the assignment, then getComments and then add, then re-merge

        try {
            if(comments == null) comments = new Assignment();
            em.getTransaction().begin();
            User user = em.find(User.class, principle);
            comments.setComments(user);
            em.persist(comments);
            em.getTransaction().commit();
            return new TransactionState<>(comments, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }*/

  //set acceptance state


    //get comments



    //get acceptance state


}
