package com.sabbreview.controller;

import com.sabbreview.model.*;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;
import com.sabbreview.NotificationService;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController extends Controller {

    /**
     * Stores an application in the database.
     * @param principle Principle (email) of the user creating the application
     * @param application Persists an application in the data
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
     * Deletes an application. Only admins and those who own an application can delete an application.
     * @param principle Principle of the user calling this function..
     * @param applicationID ID of the application to be deleted.
     */
    public static TransactionState<Application> deleteApplication(String principle, String applicationID) {
        try {

            em.getTransaction().begin();
            User user = em.find(User.class, principle);
            Application application = em.find(Application.class, applicationID);

            if(application.getApplicant().getEmailAddress().equals(user.getEmailAddress()) || user.getAdmin()){
                em.createNamedQuery("delete-application").setParameter("id", applicationID).executeUpdate();
            }

            em.getTransaction().commit();
            return new TransactionState<>(null, TransactionStatus.STATUS_OK, "");
        } catch (Exception e) {
            rollback();
            e.printStackTrace();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Gets all of the assignments which assign a given application.
     * @param applicationID The application in question
     * @return A list of assignments to which assign the application
     */
    public static TransactionState<List> getAssignments(String principle, String applicationID) {
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, principle);
            List assignments = em.createNamedQuery("get-all-assignments-for-application").setParameter("id", applicationID).getResultList();
            em.getTransaction().commit();

            //TODO replace true with an actual permission
            if( true || user.getAdmin()) {
                return new TransactionState<>(assignments, TransactionStatus.STATUS_OK, "");
            }

            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "BAD PERMISSIONS");

        } catch (Exception e) {
            rollback();
            e.printStackTrace();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Retrieves an application.
     * Principle user must either own the application, be assigned the application, or be an admin.
     * @param principle The ID of the user requesting the application.
     * @param applicationID The application to be returned.
     * @return The application.
     */
    public static TransactionState<Application> getApplication(String principle, String applicationID) {
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, principle);
            Application application;
            application = em.createNamedQuery("get-application", Application.class).setParameter("id", applicationID).getSingleResult();
            em.getTransaction().commit();

            //Checking user owns the application, is assigned the application, or is an admin
            if( userIsAssignedApplication(applicationID, user) || user.getAdmin() ||
                    application.getApplicant().getEmailAddress().equals(principle)){
                return new TransactionState<>(application, TransactionStatus.STATUS_OK, "");
            }

            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "BAD_PERMISSIONS");
        } catch (Exception e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

    /**
     * Sets the acceptance state of a given application
     * Only an admin, or someone assigned to an application can change its acceptance state.
     * @param principle Principle of the user setting the acceptance state
     * @param applicationID The application to modify
     * @param acceptanceStateString The acceptance state to set, in a string.
     */
    public static TransactionState<Application> setAcceptanceState(String principle,
                                                                   String applicationID, String acceptanceStateString) {
        try {
            AcceptanceState acceptanceState = AcceptanceState.valueOf(acceptanceStateString.toUpperCase());

            em.getTransaction().begin();
            Application application = em.find(Application.class, applicationID);
            User caller = em.find( User.class, principle);

            if( userIsAssignedApplication(applicationID, caller) || caller.getAdmin() ) {
                application.setState(acceptanceState);
            }

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
     * @param principle Principle of the calling user.
     * @param templateid Id of the template to use
     * @param departmentid Id of the department to assign the application to.
     * @return
     */
    public static TransactionState<Application> useTemplate(String principle, String templateid,
                                                            String departmentid) {
        try {
            em.getTransaction().begin();

            User user = em.find(User.class, principle);
            Department department = em.find(Department.class, departmentid);
            Template template = TemplateController.getTemplate(principle, templateid).getValue();
            queueInstance.publish(user.getEmailAddress()+"\\"+user.getEmailAddress()+"\\"+"applicationCreation");

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
     * @param principle Principle of the calling user
     * @param fieldInstanceId ID of the instance of the field whose value you want to modify.
     * @param value Value to set the field to.
     */
    public static TransactionState<FieldInstance> changeFieldValue(String principle,
                                                                   String fieldInstanceId, FieldInstanceValue value) {
        try {
            em.getTransaction().begin();
            FieldInstance fieldInstance = em.find(FieldInstance.class, fieldInstanceId);
            User caller = em.find( User.class, principle);

            //TODO add actual authentication
            if( true || caller.getAdmin()) {
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
            }

            em.getTransaction().commit();


            return new TransactionState<>(fieldInstance, TransactionStatus.STATUS_OK);
        } catch (Exception e) {
            rollback();
            e.printStackTrace();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }

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
