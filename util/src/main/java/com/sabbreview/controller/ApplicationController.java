package com.sabbreview.controller;

import com.sabbreview.model.*;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;
import com.sabbreview.NotificationService;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the high level code for operations on Application JPA objects.
 * Authentication is enforced here.
 * Call this class to do things with Applications.
 * @see Application
 */
public class ApplicationController extends Controller {

    /**
     * Stores an application object in the database.
     * @param principle The ID (email) of the user creating the application.
     * @param application Application to store.
     * @return Transaction state.
     */
    public static TransactionState<Application> createApplication(String principle,
                                                                  Application application) {
        try {

            if (application == null) {
                application = new Application();
            }

            em.getTransaction().begin();
            User user = em.find(User.class, principle);
            queueInstance.publish(user.getEmailAddress()+"\\"+user.getEmailAddress()+"\\"+"applicationCreation");
            application.setApplicant(user);
            em.persist(application);
            em.getTransaction().commit();

            return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /*
    private static TransactionState<Application> assignApplication(String principle, User applicant,
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
     */

  /**
   * Deletes an application.
   * Only admins and those who own an application can delete an application.
   * @param principle Principle (email) of the user calling this function.
   * @param applicationID ID of the application to be deleted.
   * @return Transaction state.
   */
  public static TransactionState<Application> deleteApplication(String principle, String applicationID) {
    try {
      em.getTransaction().begin();
      em.createNamedQuery("delete-application").setParameter("id", applicationID).setParameter("id", principle).executeUpdate();
      em.getTransaction().commit();
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }
  
  public static TransactionState<List> getAssignments(String principle,
      String applicationID) {
    try {
      List assignments = em.createNamedQuery("get-all-assignments-for-application").setParameter("id", applicationID).getResultList();
      return new TransactionState<>(assignments, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Retrieves an application.
   * Principle user must either own the application, be assigned the application, or be an admin.
   * @param principle The ID (email) of the user requesting the application.
   * @param applicationID The application to be returned.
   * @return The application associated with the ID as part of a transaction state.
   */
  public static TransactionState<Application> getApplication(String principle, String applicationID) {
    try {
      Application application;
      application = em.createNamedQuery("get-application", Application.class).setParameter("id", applicationID).getSingleResult();
      if (application == null) {
        return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
      }
      return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


    /**
     * Retrieves the state of an application.
     * @param principle The ID (email) of the user calling this function.
     * @param applicationID ID of the application to be processed.
     * @return The application state of the associated ID as part of a transaction state.
     */
  public static TransactionState<AcceptanceState> getState(String principle, String applicationID) {
    try {
      AcceptanceState appState;
      appState = em.createNamedQuery("get-application-state", AcceptanceState.class).setParameter("id", applicationID).getSingleResult();
      if (appState == null) {
        return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
      }
      return new TransactionState<>(appState, TransactionStatus.STATUS_OK, "");
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


  /**
   * Sets the signature of an application and stores the changes in the database.
   * @param applicationID ID of the application to be processed.
   * @param sign  String conversion of the image generated from the canvas on the application front-end page.
   * @return Transaction state.
   */
  public static TransactionState<Application> setSignature(String applicationID, String sign) {
    try {
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationID);
      application.setSignature(sign.split(",")[1]);
      em.merge(application); //need to iterate through user, find acc state, and change
      em.flush();
      em.getTransaction().commit();
      return new TransactionState<>(application, TransactionStatus.STATUS_OK);
    } catch (IllegalArgumentException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
              "Error Capturing Signature");
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Retrieves the signature string from the database.
   * @param principle The ID (email) of the user calling this function.
   * @param applicationID ID of the application to be processed.
   * @return The signature associated with the application ID as part of a transaction state.
   */
  public static TransactionState<String> getSignature(String principle, String applicationID) {
    try {
      em.getTransaction().begin();
      Application application = em.find(Application.class, applicationID);
      em.getTransaction().commit();
      return new TransactionState<>(application.getSignature(), TransactionStatus.STATUS_OK);
    } catch (IllegalArgumentException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          "Error Capturing Signature");
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Sets the acceptance state of a given application
   * Only an admin, or someone assigned to an application can change its acceptance state.
   * @param principle The ID (email) of the user setting the acceptance state
   * @param applicationID The application to modify
   * @param acceptanceStateString The acceptance state to set, in a string.
   * @return Transaction state.
   */
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
      new NotificationService().sendNotification(NotificationID.valueOf(acceptanceStateString.toUpperCase()),
              "User", application.getApplicant().getEmailAddress());//need to decide on names or not
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

  /**
   * Creates a new application using a template with templateid.
   * @param principle The ID (email) of the user calling this function.
   * @param templateid Id of the template to use
   * @param departmentid Id of the department to assign the application to.
   * @return Transaction state.
   */
  public static TransactionState<Application> useTemplate(String principle, String templateid,
      String departmentid) {
      try {
      em.getTransaction().begin();

      User user = em.find(User.class, principle);
      Department department = em.find(Department.class, departmentid);
      Template template = TemplateController.getTemplate(principle, templateid).getValue();

      if( department == null || template == null){
         return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "TEMPLATE OR" +
                        "DEPARTMENT NONE EXISTENT");
      }

      // TODO      queueInstance.publish(user.getEmailAddress()+"\\"+user.getEmailAddress()+"\\"+"applicationCreation");

      Application application = new Application();
      application.setDepartment(department);
      application.setApplicant(user);
      application.setState(AcceptanceState.PENDING);

      List<Field> fieldList = template.fieldList;
      if(fieldList  != null) {
        for (Field field : fieldList) {
          System.out.println(field);
          FieldInstance fieldInstance = new FieldInstance(field);
          em.persist(fieldInstance);
          application.addFieldInstance(fieldInstance);
                }
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

    /**
     * Changes the value of a field instance inside an application.
     * @param principle The ID (email) of the user calling this function.
     * @param fieldInstanceId ID of the instance of the field whose value you want to modify.
     * @param value Value to set the field to.
     * @return Transaction state.
     */
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
                    case LONGTEXT:
                        fieldInstance.setValue(value.getValue());
                        break;
                    case DATE:
                        //TODO
                        break;
                    case MULTICHOICE:
                        String[] choices = value.value.split(",");
                        ArrayList<FieldOption> selectedOptionsList = new ArrayList<>();
                        for (String c :
                                choices) {
                            FieldOption fieldOption = em.getReference(FieldOption.class, Integer.parseInt(c));
                            selectedOptionsList.add(fieldOption);
                        }
                        fieldInstance.setSelectedValues(selectedOptionsList);
                        break;
                    case SINGLECHOICE:
                        FieldOption singlefieldoption = em.find(FieldOption.class, Integer.parseInt(value.value));
                        if (singlefieldoption != null && fieldOf.getFieldOptions().contains(singlefieldoption)) {
                            ArrayList<FieldOption> selectedOptionList = new ArrayList<>();
                            selectedOptionList.add(singlefieldoption);
                            fieldInstance.setSelectedValues(selectedOptionList);
                        } else {
                            throw new ValidationException("Field option does not exist for this field");
                        }
                }

      em.merge(fieldInstance);
      em.getTransaction().commit();


            return new TransactionState<>(fieldInstance, TransactionStatus.STATUS_OK);
        } catch (Exception e) {
            rollback();
            e.printStackTrace();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Finds whether or not a user is assigned an application.
     * For internal use.
     * @param applicationID Application to check for.
     * @param user User to check.
     * @return Boolean of whether a user is assigned an application with applicationID.
     */
    private static boolean userIsAssignedApplication(String applicationID, User user){
        List<Assignment> assignments = user.getAssignments();
        for(int i = 0; i < assignments.size(); i++){
            if( assignments.get(i).getId().equals(applicationID) ){
                return true;
            }
        }

        return false;
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
