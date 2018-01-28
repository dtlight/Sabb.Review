package com.sabbreview;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sabbreview.adapters.UserAdadpter;
import com.sabbreview.controller.ApplicationController;
import com.sabbreview.controller.AssignmentController;
import com.sabbreview.controller.FieldController;
import com.sabbreview.controller.RoleController;
import com.sabbreview.controller.TemplateController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.User;
import com.sabbreview.responses.NotFound;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import spark.Request;

import java.net.URI;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.halt;
import static spark.Spark.notFound;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.staticFiles;


public class SabbReview {
  private static final String PERSISTENCE_UNIT_NAME = "SabbReview";
  private static final String DB_ENV_VARIABLE = "DATABASE_URL";
  public static Gson gson = generateGson();
  private static EntityManager em = getEntityManager();

  public static void main(String... args) {

    port(getHerokuAssignedPort());

    staticFiles.location("static");

    before((req, res) -> {
      res.header("Access-Control-Allow-Origin", "*");
      res.header("Access-Control-Allow-Methods", "GET, PUT, DELETE, POST");
      res.header("Access-Control-Allow-Headers", "*");
      acceptAuthentication(req);
      res.type("application/json");
      res.header("Access-Control-Allow-Origin", "*");
      res.header("Access-Control-Allow-Methods", "*");
      res.header("Access-Control-Allow-Headers", "*");
    });

    ApplicationController.attach();
    UserController.attach();
    RoleController.attach();
    FieldController.attach();
    TemplateController.attach();
    AssignmentController.attach();

    options("*", ((request, response) -> ""));

    options("*", (req, res) -> "");

    notFound((request, response) -> gson.toJson(new NotFound()));

    after("*", ((request, response) -> {
      if(getEntityManager().isOpen() && getEntityManager().getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
    }));
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
      String[] userDetails = uri.getUserInfo().split(":");
      String jdbcURL = String
          .format("jdbc:postgresql://%s:%d%s?user=%s&password=%s", uri.getHost(), uri.getPort(),
              uri.getPath(), userDetails[0], userDetails[1]);
      persistenceMap.put("javax.persistence.jdbc.url", jdbcURL);
      persistenceMap.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
      entityManagerFactory =
          Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, persistenceMap);
    } else {
      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    return entityManagerFactory.createEntityManager();
  }

  private static Gson generateGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(User.class, new UserAdadpter());
    return gsonBuilder.create();
  }



  private static void acceptAuthentication(Request req) {
    String token = req.headers("Authorization");
    if (token == null || !token.contains(".")) {
      req.attribute("isAuthenticated", false);
    } else {
      try {
        String jwtString = token.split("Bearer ")[1];
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("sabbreview").build();
        DecodedJWT decodedJWT = verifier.verify(jwtString);
        req.attribute("isAuthenticated", true);
        req.attribute("principle", decodedJWT.getClaim("principle").asString());
      } catch (Exception e) {
        e.printStackTrace();
        halt(401, gson.toJson(new TransactionState<User>(null, TransactionStatus.STATUS_ERROR,
            "Could not verify authentication token")));
      }
    }
  }
}
