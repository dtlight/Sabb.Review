package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Application;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class ApplicationController extends Controller {

  private static EntityManager em = SabbReview.getEntityManager();

  private static TransactionState<Application> createApplication(String principle,
      Application application) {
    try {
      if (application == null) {
        application = new Application();
      }
      em.getTransaction().begin();
      User user = em.find(User.class, principle);
      application.setApplicant(user);
      em.persist(application);
      em.getTransaction().commit();
      return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  private static TransactionState<Application> assignApplication(User applicant,
      Application application) {
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

  private static TransactionState<Application> deleteApplication(String principle,
      String applicationID) {
    try {
      em.getTransaction().begin();
      TypedQuery<Application> nq = em.createNamedQuery("authenticated-delete", Application.class);
      nq.setParameter("id", applicationID);
      nq.setParameter("principle", principle);

      em.getTransaction().commit();
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  private static TransactionState<Application> getApplication(String applicationID) {
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


  private static TransactionState<Application> getApplication(String applicationID,
      AcceptanceState acceptanceState) {
    Application application;
    try {
      em.getTransaction().begin();
      application = em.find(Application.class, applicationID);
      application.setState(acceptanceState);
      em.getTransaction().commit();
      return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    } finally {
      em.close();
    }
  }

  public static void attach() {
    delete("/api/application/:id", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        ApplicationController.deleteApplication(principle, req.params(":id")))));

    post("/api/application", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        ApplicationController
            .createApplication(principle, fromJson(req.body(), Application.class)))));

    get("/api/application/:id",
        (req, res) -> toJson(ApplicationController.getApplication(req.params(":id"))));

    put("/api/application/:id/state/:state", (req, res) -> toJson(ApplicationController
        .getApplication(req.params(":id"), AcceptanceState.valueOf(req.params(":state")))));

  }
}
