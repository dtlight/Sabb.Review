package com.sabbreview.controller;

import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Application;
import com.sabbreview.model.Department;
import com.sabbreview.model.Field;
import com.sabbreview.model.FieldInstance;
import com.sabbreview.model.FieldOption;
import com.sabbreview.model.FieldType;
import com.sabbreview.model.Template;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import java.util.List;
import javax.persistence.RollbackException;

public class ApplicationController extends Controller {

  public static TransactionState<Application> createApplication(String principle,
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

  /**
   * Deletes an application.
   * @param principle Principle of the user calling this function..
   * @param applicationID ID of the application to be deleted.
   * @return
   */
  public static TransactionState<Application> deleteApplication(String principle,
                                                                String applicationID) {
    try {
      em.getTransaction().begin();
      em.createNamedQuery("delete-application").setParameter("id", applicationID).setParameter("principle", principle).executeUpdate();
      em.getTransaction().commit();
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Retrieves an application.
   * @param principle The ID of the user requesting the application.
   * @param applicationID The application to be returned.
   * @return The application along with a transaction status message.
   */
  public static TransactionState<Application> getApplication(String principle, String applicationID) {
    try {
      Application application;
      application = em.createNamedQuery("get-application", Application.class).setParameter("id", applicationID).setParameter("principle", principle).getSingleResult();
      if (application == null) {
        return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
      }
      return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


  public static TransactionState<Application> setAcceptanceState(String principle,
      String applicationID, String acceptanceStateString) {
    try {
      AcceptanceState acceptanceState =
          AcceptanceState.valueOf(acceptanceStateString.toUpperCase());
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationID);
      application.setState(acceptanceState);
      em.merge(application); //need to iterate through user, find acc state, and change
      em.flush();
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


  public static TransactionState<Application> useTemplate(String principle, String templateid,
      String departmentid) {
    try {
      em.getTransaction().begin();

      User user = em.find(User.class, principle);
      Department department = em.find(Department.class, departmentid);
      Template template = TemplateController.getTemplate(principle, templateid).getValue();

      Application application = new Application();
      application.setDepartment(department);
      application.setApplicant(user);
      application.setState(AcceptanceState.PENDING);

      List<Field> fieldList = template.fieldList;

      for (Field field : fieldList) {
        System.out.println(field);
        FieldInstance fieldInstance = new FieldInstance(field);
        em.persist(fieldInstance);
        application.addFieldInstance(fieldInstance);
      }
      em.persist(application);
      em.merge(application);
      em.merge(user);
      em.merge(department);


      em.flush();
      em.getTransaction().commit();
      return new TransactionState<>(application, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  public static TransactionState<FieldInstance> changeFieldValue(String principle,
      String fieldInstanceId, FieldInstanceValue value) {
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
          fieldInstance.setValue(value.getValue());
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


  public class FieldInstanceValue {
    String value;

    public String getValue() {
      return value;
    }

    public FieldInstanceValue setValue(String value) {
      this.value = value;
      return this;
    }

    @Override public String toString() {
      return "FieldInstanceValue{" + "value='" + value + '\'' + '}';
    }
  }
}
