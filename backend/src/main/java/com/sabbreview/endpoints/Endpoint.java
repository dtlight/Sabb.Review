package com.sabbreview.endpoints;

import com.google.gson.Gson;
import com.sabbreview.AuthentcatedRequest;
import com.sabbreview.SabbReview;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import spark.Request;
import spark.Spark;

class Endpoint {
  static String requireAuthentication(Request req, AuthentcatedRequest result) {
    if (req.attribute("isAuthenticated")) {
      return result.onAccept(req.attribute("principle"));
    } else {
      Spark.halt(401, new Gson().toJson(new TransactionState<User>(null, TransactionStatus.STATUS_ERROR,
          "Could not verify authentication token")));
      return null;
    }
  }

  static String toJson(TransactionState transactionState) {
    return SabbReview.gson.toJson(transactionState);
  }

  static <T> T fromJson(String string, Class<T> classOfT) {
    return SabbReview.gson.fromJson(string, classOfT);
  }
}
