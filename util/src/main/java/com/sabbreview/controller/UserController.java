package com.sabbreview.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.Token;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

public class UserController extends Controller {
  private static final String EMAIL_REGEX;

  static {
    EMAIL_REGEX = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
  }

  public static TransactionState<User> registerUser(User user) {
    try {
      em.getTransaction().begin();
      user.encryptPassword();
      if (!user.getEmailAddress().matches(EMAIL_REGEX)) {
        throw new ValidationException("emailAddress");
      }

      em.persist(user);
      em.getTransaction().commit();
      return new TransactionState<>(user, TransactionStatus.STATUS_OK);
    } catch (RollbackException e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          (e.getCause().getMessage().contains("duplicate") ?
              "There is already a user with that email address" :
              null));
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    }
  }

  public static TransactionState<User> getUser(String emailAddress) {
    User user = em.find(User.class, emailAddress);
    if (user == null) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not find user");
    } else {
      return new TransactionState<>(user, TransactionStatus.STATUS_OK);
    }
  }

  public static TransactionState<Token> generateSession(UserAuthenticationParameters uap) {
    String token;
    try {
      User user = em.find(User.class, uap.getEmailAddress());
      if(System.getenv("SECURE_KEY") == null) {
        throw new Exception("Could not securely create session");
      } else {
        if (user != null && user.verifyPassword(uap.getPassword())) {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(new Date());
          calendar.add(Calendar.DAY_OF_YEAR, 3);
          Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECURE_KEY"));
          token = JWT.create().withIssuer("sabbreview").withClaim("principle", user.getEmailAddress())
              .withExpiresAt(calendar.getTime()).sign(algorithm);
          Token instance = new Token(token);
          instance.setAdmin(user.getAdmin());

          return new TransactionState<>(instance, TransactionStatus.STATUS_OK);

        } else {
          throw new ValidationException();
        }
      }

    } catch (ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not verify user");
    } catch (UnsupportedEncodingException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Internal Exception");
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    }

  }

  public static TransactionState<User> deleteUser(String principle) {
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


  public static TransactionState<User> promoteUser(String principle, String id) {
    try {
      em.getTransaction().begin();
      User user = em.find(User.class, id);
      if(user == null) {
        throw new ValidationException();
      } else {
        user.setAdmin(true);
      }
      em.merge(user);
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

  public static TransactionState<List<Application>> getApplicationsForUser(String principle) {
    try {
      TypedQuery<Application> applicationTypedQuery = em.createNamedQuery("get-all-applications-for-user", Application.class);
      applicationTypedQuery.setParameter("principle", principle);
      List<Application> applicationList = applicationTypedQuery.getResultList();
      return new TransactionState<>(applicationList, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      e.printStackTrace();
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    }
  }

  public static TransactionState<List<User>> getAllUsers(String principle) {
    try {
      User user = em.find(User.class, principle);
      TypedQuery<User> userTypedQuery = em.createNamedQuery("get-all-users", User.class);
      userTypedQuery.setParameter("isAdmin", user.getAdmin());
      return new TransactionState<>(userTypedQuery.getResultList(), TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      e.printStackTrace();
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    }
  }


  public static TransactionState<List<Assignment>> getAssignmentsForUser(String principle) {
    try {
      TypedQuery<Assignment> assignmentTypedQuery = em.createNamedQuery("get-all-assignments-for-user", Assignment.class);
      assignmentTypedQuery.setParameter("owner", principle);
      List<Assignment> assignmentList = assignmentTypedQuery.getResultList();
      return new TransactionState<>(assignmentList, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      e.printStackTrace();
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    }
  }

  public class UserAuthenticationParameters {
    String emailAddress = "";
    String password = "";

    public UserAuthenticationParameters() {
    }

    String getEmailAddress() {
      return emailAddress;
    }

    public UserAuthenticationParameters setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
      return this;
    }

    String getPassword() {
      return password;
    }

    public UserAuthenticationParameters setPassword(String password) {
      this.password = password;
      return this;
    }

    @Override public String toString() {
      return super.toString();
    }
  }
}

