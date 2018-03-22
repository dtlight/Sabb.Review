package com.sabbreview.controller;

import com.sabbreview.model.Department;
import com.sabbreview.model.Field;
import com.sabbreview.model.Template;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

/**
 * Contains the high level code for operations on Assignment objects.
 * Authentication is currently *not* enforced with roles.
 * @see Template
 */
public class TemplateController extends Controller {

  /**
   * Creates an application template.
   * @param principle ID (email) of the calling user.
   * @param name A name (label) for the template to create. Should be unique.
   * @param departmentId ID of the department to assign the application to.
   * @return Transaction state.
   */
  public static TransactionState<Template> createTemplate(String principle, String name, String departmentId) {
    try {
      em.getTransaction().begin();
      Department department = em.find(Department.class, departmentId);
      Template template = new Template();
      template.setName(name);
      template.setDepartment(department);
      em.persist(template);
      em.getTransaction().commit();
      return new TransactionState<>(template, TransactionStatus.STATUS_OK, "Template created");
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


  /**
   * Deletes an application template.
   * @param principle ID (email) of the calling user.
   * @param id Id of the template to delete.
   * @return Transaction state.
   */
  public static TransactionState<Template> deleteTemplate(String principle, String id) {
    try {
      em.getTransaction().begin();
      Template template = em.find(Template.class, id);
      em.remove(template);
      em.getTransaction().commit();
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "Template deleted");
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Deletes a field that belongs to a template
   * @param principle ID (email) of the calling user.
   * @param id ID of the template to modify.
   * @param fieldId ID of the field to delete.
   * @return Transaction state.
   */
  public static TransactionState<Template> deleteTemplateField(String principle, String id,
      String fieldId) {
    try {
      em.getTransaction().begin();
      Template template = em.find(Template.class, id);
      Field field = em.find(Field.class, fieldId);
      if(template != null && field != null) {
        template.getFieldList().remove(field);
      }
      em.persist(template);
      em.getTransaction().commit();
      return new TransactionState<>(template, TransactionStatus.STATUS_OK, "Template field deleted");
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Fetches an application template.
   * @param principle ID (email) of the callling user.
   * @param id ID of the template to fetch.
   * @return The template with the associated ID as part of a transaction state.
   */
  public static TransactionState<Template> getTemplate(String principle, String id) {
    try {
      Template template = em.find(Template.class, id);
      if(template == null) {
        throw new ValidationException("Template not found");
      }
      return new TransactionState<>(template, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }

  /**
   * Adds a field to an application template.
   * @param principle ID (email) of the calling user.
   * @param templateID ID of the template to modify.
   * @param field The field to add to the template.
   * @return Transaction state.
   */
  public static TransactionState<Template> addField(String principle, String templateID, Field field) {
    try {
      em.getTransaction().begin();
      if(field == null) {
        throw new ValidationException();
      }
      Template template = em.find(Template.class, templateID);
      template.addField(field);
      em.merge(template);
      em.getTransaction().commit();
      return new TransactionState<>(template , TransactionStatus.STATUS_OK, "Field Added to Template");
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    } catch (ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Invalid Field");
    }
  }
}
