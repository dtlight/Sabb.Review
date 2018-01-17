
package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.EntityManager;

public class ApplicationController {

    private static EntityManager em = SabbReview.getEntityManager();

    public static TransactionState<User> registerApplication(User applicant) {

        //I need to create setters and getters for applicant and then create a validation condition for each getter?
        try{
            em.getTransaction().begin();
            if(!applicant.get().matches("")){
                throw new ValidationException("empty field");
            } else {
                em.persist(applicant);
            }
        } catch (ValidationException e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
        } finally {
            em.getTransaction().commit();
        }

        return new TransactionState<>(applicant, TransactionStatus.STATUS_OK);
    }

    public static TransactionState<User> getApplicant(String id) {
        User applicant = em.find(User.class, id);
        if (applicant == null) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not find applicant");
        } else {
            return new TransactionState<>(applicant, TransactionStatus.STATUS_OK);
        }
    }


}
