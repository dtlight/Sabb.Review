package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.User;
import com.sabbreview.model.Application;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import javax.persistence.EntityManager;

public class AssignmentController {
    private static EntityManager em = SabbReview.getEntityManager();

    public static TransactionState<Assignment> setOwner(User owner, Application assignment) {
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
}
