package com.sabbreview.controller;

import com.sabbreview.model.Template;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

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


  public static void attach() {
    post("/template", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createTemplate(principle, fromJson(req.body(), Template.class))))));

    delete("/template/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(deleteTemplate(principle, req.params(":id"))))));
  }
}
