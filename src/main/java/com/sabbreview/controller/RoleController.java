package com.sabbreview.controller;

import com.sabbreview.model.Role;
import com.sabbreview.model.User;
import com.sabbreview.responses.AuthenticationException;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.RollbackException;

import static spark.Spark.delete;
import static spark.Spark.post;

public class RoleController extends Controller {

  private static TransactionState<Role> createRole(String principle, Role role) {
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

  private static TransactionState<Role> removeRole(String principle, String id) {
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

  public static void attach() {
    delete("/api/role/:id", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(RoleController.removeRole(principle, req.params("id")))));

    post("/api/role", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        RoleController.createRole(principle, fromJson(req.body(), Role.class)))));
  }
}
