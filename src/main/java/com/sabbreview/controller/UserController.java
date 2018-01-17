package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

public class UserController {
  private static EntityManager em = SabbReview.getEntityManager();

  public static TransactionState<User> registerUser(User user) {
    try {
      user.encryptPassword();
      em.getTransaction().begin();
      em.persist(user);
      em.getTransaction().commit();
    } catch (RollbackException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          (e.getCause().getMessage().contains("duplicate") ?
              "There is already a user with that email address" :
              null));
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


  public static TransactionState<User> generateSession(String emailAddress, String password) {
    User user = em.find(User.class, emailAddress);
    //TODO The rest of the JWT shiz (Kal)
    if (user == null) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not find user");
    } else {
      return new TransactionState<>(user, TransactionStatus.STATUS_OK);
    }
  }

}
