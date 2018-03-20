package com.sabbreview.controller;

import com.sabbreview.model.Role;
import com.sabbreview.model.User;
import com.sabbreview.responses.AuthenticationException;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

/**
 * Contains the high level code for operations on user roles.
 * Authentication is currently *not* enforced with roles.
 * Call this class to perform operations on roles.
 * @see Role
 */
public class RoleController extends Controller {

  public static TransactionState<Role> createRole(String principle, Role role) {
    try {
      User userPrinciple = em.find(User.class, principle);
      if (userPrinciple != null && userPrinciple.isAdmin) {
        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();
        return new TransactionState<>(role, TransactionStatus.STATUS_OK, null);
      } else {
        throw new AuthenticationException();
      }
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          "Database rejected new role");

    } catch (AuthenticationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "User not authorized");
    }
  }

  public static TransactionState<Role> removeRole(String principle, String id) {
    try {
      User userPrinciple = em.find(User.class, principle);
      if (userPrinciple.isAdmin) {
        em.getTransaction().begin();
        Role role = em.find(Role.class, id);
        if (role != null) {
          em.remove(role);
        } else {
          throw new ValidationException();
        }
        em.getTransaction().commit();
        return new TransactionState<>(null, TransactionStatus.STATUS_OK, null);
      } else {
        throw new AuthenticationException();
      }
    } catch (RollbackException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR,
          "Database rejected role deletion");
    } catch (AuthenticationException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "User not authorized");
    } catch (ValidationException | NumberFormatException e) {
      rollback();
      return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Role does not exist");
    }
  }
}
