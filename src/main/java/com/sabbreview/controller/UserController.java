package com.sabbreview.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sabbreview.SabbReview;
import com.sabbreview.model.Token;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

public class UserController {
  private static final String EMAIL_REGEX = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
  private static EntityManager em = SabbReview.getEntityManager();


  public static TransactionState<User> registerUser(User user) {
    try {
      user.encryptPassword();
      em.getTransaction().begin();
      if (!user.getEmailAddress().matches(EMAIL_REGEX)) {
        throw new ValidationException("emailAddress");
      } else {
        em.persist(user);
      }
      em.getTransaction().commit();
    } catch (RollbackException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          (e.getCause().getMessage().contains("duplicate") ?
              "There is already a user with that email address" :
              null));
    } catch (ValidationException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
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


  public static TransactionState<Token> generateSession(String emailAddress, String password) {
    String token;
    try {
      User user = em.find(User.class, emailAddress);
      if (user != null && user.verifyPassword(password)) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        Algorithm algorithm = Algorithm.HMAC256("secret");
        token = JWT.create().withIssuer("sabbreview").withClaim("principle", user.getEmailAddress())
            .withExpiresAt(calendar.getTime()).sign(algorithm);
      } else {
        throw new ValidationException();
      }
    } catch (ValidationException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not verify user");
    } catch (UnsupportedEncodingException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Internal Exception");
    }
    return new TransactionState<>(new Token(token), TransactionStatus.STATUS_OK);

  }

  public static TransactionState<User> deleteUser(User user) {
    try {
      em.getTransaction().begin();
      em.remove(user);
    } catch (RollbackException e) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    } finally {
      em.getTransaction().commit();
    }
    return new TransactionState<>(user, TransactionStatus.STATUS_OK);
  }
}
