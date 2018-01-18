package com.sabbreview;

import com.google.gson.Gson;
import com.sabbreview.controller.ApplicationController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.Application;
import com.sabbreview.model.User;
import com.sabbreview.responses.NotFound;
import com.sabbreview.responses.TransactionState;

import java.net.URI;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.*;

public class SabbReview {
  private static final String PERSISTENCE_UNIT_NAME = "SabbReview";
  private static final String DB_ENV_VARIABLE = "POSTGRES_GOOGLE";
  private static Gson gson = new Gson();
  private static EntityManager em = getEntityManager();

  public static void main(String... args) {

    port(getHerokuAssignedPort());

    staticFiles.location("static");

    before((req, res) -> res.type("application/json"));

    post("/api/user",
        (req, res) -> toJson(UserController.registerUser(fromJson(req.body(), User.class))));

    get("/api/user/:id", (req, res) -> toJson(UserController.getUser(req.params("id"))));


    delete("/api/application", (req, res) -> toJson(ApplicationController.deleteApplication(fromJson(req.body(), Application.class))));

    get("/api/application/:id", (req, res) -> toJson(ApplicationController.getApplication(req.params(":id"))));

    post("/api/application", (req, res) -> toJson(ApplicationController.createApplication(fromJson(req.body(), Application.class)));

    post("/api/application", (req, res) -> toJson(ApplicationController.assignApplication(fromJson(req.body(), Application.class)));



    notFound((request, response) -> new NotFound().toJSON());



  }



  private static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567;
  }

  public static EntityManager getEntityManager() {
    EntityManagerFactory entityManagerFactory;
    if (System.getenv(DB_ENV_VARIABLE) != null) {
      URI uri = URI.create(System.getenv(DB_ENV_VARIABLE));
      HashMap<String, String> persistenceMap = new HashMap<>();
      persistenceMap.put("javax.persistence.jdbc.url",
          "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath()
              + "?sslmode=require");
      persistenceMap.put("javax.persistence.jdbc.user", uri.getUserInfo().split(":")[0]);
      persistenceMap.put("javax.persistence.jdbc.password", uri.getUserInfo().split(":")[1]);
      persistenceMap.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
      entityManagerFactory =
          Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, persistenceMap);
    } else {
      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    return entityManagerFactory.createEntityManager();
  }

  private static String toJson(TransactionState transactionState) {
    return gson.toJson(transactionState);
  }

  private static <T> T fromJson(String string, Class<T> classOfT) {
    return gson.fromJson(string, classOfT);
  }
}
