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

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class UserController extends Controller {
  private static final String EMAIL_REGEX;

  static {
    EMAIL_REGEX = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
  }

  private static EntityManager em = SabbReview.getEntityManager();


  private static TransactionState<User> registerUser(User user) {
    try {
      user.encryptPassword();
      em.getTransaction().begin();
      user.setAdmin(true); //TODO GET RID OF THIS (obviously)
      if (!user.getEmailAddress().matches(EMAIL_REGEX)) {
        throw new ValidationException("emailAddress");
      } else {
        em.persist(user);
      }
      em.getTransaction().commit();
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          (e.getCause().getMessage().contains("duplicate") ?
              "There is already a user with that email address" :
              null));
    } catch (ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    }
    return new TransactionState<>(user, TransactionStatus.STATUS_OK);
  }

  private static TransactionState<User> getUser(String emailAddress) {
    User user = em.find(User.class, emailAddress);
    if (user == null) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not find user");
    } else {
      return new TransactionState<>(user, TransactionStatus.STATUS_OK);
    }
  }


  private static TransactionState<Token> generateSession(String emailAddress, String password) {
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
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not verify user");
    } catch (UnsupportedEncodingException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Internal Exception");
    }
    return new TransactionState<>(new Token(token), TransactionStatus.STATUS_OK);

  }

  private static TransactionState<User> deleteUser(String principle) {
    try {
      em.getTransaction().begin();
      User user = em.find(User.class, principle);
      if(user == null) {
        throw new ValidationException();
      } else {
        em.remove(user);
      }
      em.getTransaction().commit();
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    } catch (ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Invalid user principle");
    }
    return new TransactionState<>(null, TransactionStatus.STATUS_OK);
  }



  public static void attach() {
    delete("/api/user",
        (req, res) -> requireAuthentication(req, (principle) -> toJson(UserController.deleteUser(principle))));

    post("/api/user",
        (req, res) -> toJson(UserController.registerUser(fromJson(req.body(), User.class))));

    get("/api/user/:id", (req, res) -> toJson(UserController.getUser(req.params("id"))));

    post("/api/login", (req, res) -> toJson(UserController
        .generateSession(req.queryParams("emailAddress"), req.queryParams("password"))));

    get("/api/user", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(UserController.getUser(principle))));
  }
}
