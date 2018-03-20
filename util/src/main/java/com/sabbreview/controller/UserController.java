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

/**
 * Contains the high level code for operations on User JPA Objects.
 * Authentication is enforced for some methods in this class.
 * Call this class to do operations on Users.
 * @see User
 */
public class UserController extends Controller {
  private static final String EMAIL_REGEX;

  static {
    EMAIL_REGEX = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
  }

  /**
   * Stores a user object in the database.
   * ALL USERS ARE CREATED AS ADMINS CURRENTLY.
   * SEE TODO!!!
   * @param user User to persist.
   */
  public static TransactionState<User> registerUser(User user) {
    try {
      em.getTransaction().begin();
      user.encryptPassword();
      user.setAdmin(true); //TODO GET RID OF THIS (obviously)
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

  /**
   * Fetches a user object.
   * @param emailAddress Email address of user to fetch.
   * @return The user object, as part of a transactionstate.
   */
  public static TransactionState<User> getUser(String emailAddress) {
    User user = em.find(User.class, emailAddress);
    if (user == null) {
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not find user");
    } else {
      return new TransactionState<>(user, TransactionStatus.STATUS_OK);
    }
  }

  /**
   * Generates a session token.
   * @param uap
   * @return A token, as part of a transactionstate.
   */
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
    return new TransactionState<>(new Token(token), TransactionStatus.STATUS_OK);

  }

  /**
   * Deletes the user that calls this function.
   * @param principle Principle of the calling user.
   */
  public static TransactionState<User> deleteUser(String principle) {
    try {
      User user = em.find(User.class, principle);
      if(user.getEmailAddress().equals(principle)) {

        em.getTransaction().begin();
        if (user == null) {
          throw new ValidationException();
        } else {
          em.remove(user);
        }
        em.getTransaction().commit();

      }
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    } catch (ValidationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Invalid user principle");
    }
    return new TransactionState<>(null, TransactionStatus.STATUS_OK);
  }

  /**
   * Fetches all of the applications belonging to the calling user.
   * @param principle Principle of the calling user.
   * @return A list of applications belonging to the user.
   */
  public static TransactionState<List<Application>> getApplicationsForUser(String principle) {
    try {
      TypedQuery<Application> applicationTypedQuery = em.createNamedQuery("get-all-applications-for-user", Application.class);
      applicationTypedQuery.setParameter("id", principle);
      List<Application> applicationList = applicationTypedQuery.getResultList();
      return new TransactionState<>(applicationList, TransactionStatus.STATUS_OK);
    } catch (Exception e) {
      e.printStackTrace();
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, e.getMessage());
    }
  }


  /**
   * Fetches all the assignments for the calling user.
   * @param principle Principle of the calling user.
   * @return A list of Assignments.
   */
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

