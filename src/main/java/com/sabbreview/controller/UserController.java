package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

public class UserController {

  private static final String EMAIL_REGEX = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
  private static EntityManager em = SabbReview.getEntityManager();

  public static TransactionState<User> registerUser(User user) {
    try {
      em.getTransaction().begin();
      if (!user.getEmailAddress().matches(EMAIL_REGEX)) {
        throw new ValidationException("emailAddress");
      } else {
        em.persist(user);
      }
    } catch (RollbackException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          (e.getCause().getMessage().contains("duplicate") ?
              "There is already a user with that email address" :
              null));
    } catch (ValidationException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    } finally {
      em.getTransaction().commit();
    }
    return new TransactionState<>(user, TransactionStatus.STATUS_OK);
  }

  public static TransactionState<User> getUser(String emailAddress) {
    User user = em.find(User.class, emailAddress);
    if (user == null) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not find user");
    } else {
      return new TransactionState<>(user, TransactionStatus.STATUS_OK);
    }
  }


}
