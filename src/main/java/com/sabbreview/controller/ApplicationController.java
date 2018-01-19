
package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.Application;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ApplicationController {

    private static EntityManager em = SabbReview.getEntityManager();

    public static TransactionState<Application> createApplication(String principle, Application application) {
        try {
            if(application == null) application = new Application();
            em.getTransaction().begin();
            User user = em.find(User.class, principle);
            application.setApplicant(user);
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

    public static TransactionState<Application> deleteApplication(String principle, Application application) {
        try {
            em.getTransaction().begin();
          TypedQuery<Application> nq =
              em.createNamedQuery("authenticated-delete", Application.class);
            nq.setParameter("id", application.getId());
          nq.setParameter("principle", principle);

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
