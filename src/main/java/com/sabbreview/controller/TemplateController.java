package com.sabbreview.controller;

import com.sabbreview.model.Field;
import com.sabbreview.model.Template;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

import static spark.Spark.delete;
import static spark.Spark.post;


public class TemplateController extends Controller {

  private static TransactionState<Template> createTemplate(String principle, Template template) {
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


  private static TransactionState<Template> deleteTemplate(String principle, String id) {
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


  private static TransactionState<Template> addField(String principle, String id, Field field) {
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

  public static void attach() {
    post("/template", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createTemplate(principle, fromJson(req.body(), Template.class))))));

    post("/api/template/:id/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(addField(principle, req.params("id"), fromJson(req.body(), Field.class))))));

    delete("/api/template/:id", (req, res) -> requireAuthentication(req,
      delete("/template/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(deleteTemplate(principle, req.params(":id"))))));
  }
}
