package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Application;
import com.sabbreview.model.Field;
import com.sabbreview.model.FieldInstance;
import com.sabbreview.model.FieldOption;
import com.sabbreview.model.FieldType;
import com.sabbreview.model.Template;
import com.sabbreview.model.User;
import com.sabbreview.responses.AuthenticationException;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

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
      rollback();
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
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  private static TransactionState<Application> deleteApplication(String principle,
      String applicationID) {
    try {
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationID);
      if (application.getApplicant().getEmailAddress().equals(principle)) {
        em.remove(application);
      } else {
        throw new AuthenticationException();
      }
      em.getTransaction().commit();
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  private static TransactionState<Application> getApplication(String applicationID) {
    try {
      Application application;
      application = em.find(Application.class, applicationID);
      return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


  private static TransactionState<Application> setAcceptanceState(String applicationID,
      String acceptanceStateString) {
    try {
      AcceptanceState acceptanceState =
          AcceptanceState.valueOf(acceptanceStateString.toUpperCase());
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationID);
      application.setState(acceptanceState);
      em.getTransaction().commit();
      return new TransactionState<>(application, TransactionStatus.STATUS_OK);
    } catch (IllegalArgumentException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          "Invalid Acceptance State");
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  public static TransactionState<Application> useTemplate(String principle, String applicationid,
      String templateid) {
    try {
      em.getTransaction().begin();
      Application application;
      if (applicationid == null) {
        application = new Application();
      } else {
        application = em.find(Application.class, applicationid);
      }
      Template template = em.find(Template.class, templateid);

      if (application == null || template == null) {
        throw new ValidationException(
            (application == null) ? "Assignment cannot be found" : "Template cannot be found");
      }

      List<Field> fieldList = template.getFieldList();

      for (Field field : fieldList) {
        FieldInstance fieldInstance = new FieldInstance(field);
        em.persist(fieldInstance);
        application.addFieldInstance(fieldInstance);
      }
      if (applicationid == null) {
        em.persist(application);
      } else {
        em.merge(application);
      }
      em.getTransaction().commit();
      return new TransactionState<>(application, TransactionStatus.STATUS_OK);
    } catch (ValidationException | RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  public static TransactionState<FieldInstance> changeFieldValue(String principle,
      String fieldInstanceId, String value) {
    try {
      em.getTransaction().begin();
      FieldInstance fieldInstance = em.find(FieldInstance.class, fieldInstanceId);

      if (fieldInstance == null) {
        throw new ValidationException("Field Instance does not exist");
      }

      Field fieldOf = fieldInstance.getField();
      FieldType fieldType = fieldOf.getType();

      switch (fieldType) {
        case TEXT:
          fieldInstance.setValue(value);
          break;
        case DATE:
          //TODO
          break;
        case MULTICHOICE:
          FieldOption option = em.find(FieldOption.class, value);
          if (option != null && fieldOf.getFieldOptions().contains(option)) {
            fieldInstance.setOption(option);
          } else {
            throw new ValidationException("Field option does not exist for this field");
          }
      }

      em.merge(fieldInstance);
      em.getTransaction().commit();


      return new TransactionState<>(fieldInstance, TransactionStatus.STATUS_OK);
    } catch (ValidationException | RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
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

    put("/api/application/:applicationid/template/:templateid",
        (req, res) -> requireAuthentication(req, (principle) -> toJson(ApplicationController
            .useTemplate(principle, req.params("applicationid"), req.params("templateid")))));

    post("/api/application/template/:templateid", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(
            ApplicationController.useTemplate(principle, null, req.params("templateid")))));

    put("/api/application/:id/state/:state", (req, res) -> toJson(
        ApplicationController.setAcceptanceState(req.params(":id"), req.params(":state"))));


    put("/api/fieldinstance/:id/value/:value", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(ApplicationController
            .changeFieldValue(principle, req.params(":id"), req.params("value")))));

   /* put("/api/application/:id/state/:state", (req, res) -> toJson(ApplicationController
        .setAcceptanceState(req.params(":id"), AcceptanceState.valueOf(req.params(":state")))));

    put("/api/application/:id/state/:state", (req, res) -> toJson(ApplicationController
        .setAcceptanceState(req.params(":id"), AcceptanceState.valueOf(req.params(":state")))));
*/
  }
}
