package com.sabbreview.controller;

import com.sabbreview.model.Field;
import com.sabbreview.model.FieldOption;
import com.sabbreview.model.FieldType;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;


/**
 * Contains the high level code for operations on template field JPA objects.
 * Authentication is enforced here.
 * Call this class to do things with Fields.
 * @see Field
 * @see com.sabbreview.model.Template
 * @see com.sabbreview.model.FieldInstance
 */
public class FieldController extends Controller {

  /**
   * Stores a field in the database.
   * @param principle ID (email) of the calling user.
   * @param field Field to persist.
   * @return Transaction state.
   */
  public static TransactionState<Field> createField(String principle, Field field) {

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

  /**
   * Modifies a field.
   * @param principle ID (email) of the calling user.
   * @param field Modified field, to be stored.
   * @return Transaction state.
   */
  public static TransactionState<Field> editField(String principle, Field field) {
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

  /**
   * Fetches a field from the database.
   * @param principle ID (email) of the calling user.
   * @param id ID of the field to fetch.
   * @return The field with the associated ID as part of a transaction state.
   */
  public static TransactionState<Field> getField(String principle, String id) {
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

  /**
   * Deletes a field.
   * @param principle ID (email) of the calling user.
   * @param id ID of the field to delete.
   * @return Transaction state.
   */
  public static TransactionState<Field> deleteField(String principle, String id) {
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

  /**
   * Adds an option to a multi-choice question.
   * @param principle ID (email) of the calling user.
   * @param id ID of the field to add an option to. Should be of type multichoice.
   * @param fieldOption Option to add to the field.
   * @return Transaction state.
   */
  public static TransactionState<Field> addOption(String principle, String id,
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
}
