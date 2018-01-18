
package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.Application;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import javax.persistence.EntityManager;

public class ApplicationController {

    private static EntityManager em = SabbReview.getEntityManager();

    public static TransactionState<Application> createApplication(Application application) {
        try{
            em.getTransaction().begin();
            em.persist(application);
            em.getTransaction().commit();
            return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Application> assignApplication(User applicant, Application application) {
        try {
            em.getTransaction().begin();
            application.setApplicant(applicant);
            em.merge(application);
            em.getTransaction().commit();
            return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Application> deleteApplication(Application application) {
        try {
            em.getTransaction().begin();
            em.remove(application);
            em.getTransaction().commit();
            return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    public static TransactionState<Application> getApplication(String applicationID) {
        Application application;
        try {
            em.getTransaction().begin();
            application = em.find(Application.class, applicationID);
            em.getTransaction().commit();
            return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }


    }
