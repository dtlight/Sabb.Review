package com.sabbreview.controller;

import com.sabbreview.model.Field;
import com.sabbreview.model.FieldOption;
import com.sabbreview.model.FieldType;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;


public class FieldController extends Controller {

  private static TransactionState<Field> createField(String principle, Field field) {
    try {
      em.getTransaction().begin();
      em.persist(field);
      em.getTransaction().commit();
      return new TransactionState<>(field, TransactionStatus.STATUS_OK, "Field created");
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


  private static TransactionState<Field> editField(String principle, Field field) {
    try {
      em.getTransaction().begin();
      em.merge(field);
      em.getTransaction().commit();
      return new TransactionState<>(field, TransactionStatus.STATUS_OK, "Field edited");
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    }
  }


  private static TransactionState<Field> getField(String principle, String id) {
    try {
      Field field = em.find(Field.class, id);
      if(field == null) {
        throw new ValidationException("Field does not exists");
      }
      return new TransactionState<>(field, TransactionStatus.STATUS_OK);
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    } catch (ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getValidationField());

    }
  }

  private static TransactionState<Field> deleteField(String principle, String id) {
    try {
      em.getTransaction().begin();
      Field template = em.find(Field.class, id);
      if(template == null) {
        throw new ValidationException("Field does not exist");
      }
      em.remove(template);
      em.getTransaction().commit();
      return new TransactionState<>(null, TransactionStatus.STATUS_OK, "Field deleted");
    } catch (RollbackException | ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Field could not be deleted");
    }
  }

  private static TransactionState<Field> addOption(String principle, String id,
      FieldOption fieldOption) {
    try {
      em.getTransaction().begin();
      Field field = em.find(Field.class, id);
      if(field == null) {
        throw new ValidationException();
      }
      FieldType fieldType = field.getType();
      switch (fieldType) {
        case TEXT:
          throw new ValidationException("Text fields cannot have an option");
        case LONGTEXT:
          throw new ValidationException("Text fields cannot have an option");
        case DATE:
          throw new ValidationException("Date fields cannot have an option");
        case DIVIDER:
          throw new ValidationException("Dividers cannot have an option");
        default:
          if(em.find(FieldOption.class, fieldOption.getTitle()) == null) {
            em.persist(fieldOption);
          }
          field.addFieldOption(fieldOption);
          em.merge(field);
      }
      em.getTransaction().commit();
      return new TransactionState<>(field, TransactionStatus.STATUS_OK, "Field option created");
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
    } catch (ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getValidationField());
    }
  }




  public static void attach() {
    post("/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createField(principle, fromJson(req.body(), Field.class))))));
    put("/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(editField(principle, fromJson(req.body(), Field.class))))));

    post("/field/:id/option", (req, res) -> requireAuthentication(req,
        (principle -> toJson(addOption(principle, req.params(":id"), fromJson(req.body(), FieldOption.class))))));

    delete("/field/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(deleteField(principle, req.params(":id"))))));

    get("/field/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(getField(principle, req.params(":id"))))));
    get("/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(getField(principle, req.params(":id"))))));
  }
}
