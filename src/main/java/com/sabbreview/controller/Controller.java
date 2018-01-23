package com.sabbreview.controller;

import com.sabbreview.AuthentcatedRequest;
import com.sabbreview.SabbReview;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import spark.Request;

import javax.persistence.EntityManager;

import static spark.Spark.halt;

class Controller {
  static EntityManager em = SabbReview.getEntityManager();


  static String toJson(TransactionState transactionState) {
    return SabbReview.gson.toJson(transactionState);
  }

  static <T> T fromJson(String string, Class<T> classOfT) {
    return SabbReview.gson.fromJson(string, classOfT);
  }

  static String requireAuthentication(Request req, AuthentcatedRequest result) {
    if (req.attribute("isAuthenticated")) {
      return result.onAccept(req.attribute("principle"));
    } else {
      halt(401, toJson(new TransactionState<User>(null, TransactionStatus.STATUS_ERROR,
          "Could not verify authentication token")));
      return null;
    }
  }

  static void rollback() {
    if(em.getTransaction().isActive()){
      em.getTransaction().rollback();
    }
  }
}
