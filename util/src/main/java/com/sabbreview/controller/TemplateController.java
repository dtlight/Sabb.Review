package com.sabbreview.controller;

import com.sabbreview.model.Field;
import com.sabbreview.model.Template;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;


public class TemplateController extends Controller {

  public static TransactionState<Template> createTemplate(String principle, Template template) {
    try {
      em.getTransaction().begin();
      em.persist(template);
      em.getTransaction().commit();
      return new TransactionState<>(template, TransactionStatus.STATUS_OK, "Template created");
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


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

  public static TransactionState<Template> addField(String principle, String id, Field field) {
    try {
      em.getTransaction().begin();
      if(field == null) {
        throw new ValidationException();
      }
      Template template = em.find(Template.class, id);
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