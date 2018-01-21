package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import javax.persistence.EntityManager;

public class AssignmentController {
    private static EntityManager em = SabbReview.getEntityManager();

    public static TransactionState<Assignment> setOwner(User owner, Assignment assignment) {
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

    public static TransactionState<Assignment> getOwner(String owner) {
        Assignment assignment;
        try {
            em.getTransaction().begin();
            assignment = em.find(Assignment.class, owner);
            em.getTransaction().commit();
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Assignment> setComments(String principle, Assignment comments) {
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
    }

  //set acceptance state


    //get comments



    //get acceptance state


}
