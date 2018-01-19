package com.sabbreview;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sabbreview.adapters.UserAdadpter;
import com.sabbreview.controller.ApplicationController;
import com.sabbreview.controller.UserController;
import com.sabbreview.model.Application;
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

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.notFound;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;


public class SabbReview {
  private static final String PERSISTENCE_UNIT_NAME = "SabbReview";
  private static final String DB_ENV_VARIABLE = "DATABASE_URL";
  private static Gson gson = generateGson();
  private static EntityManager em = getEntityManager();

  public static void main(String... args) {

    port(getHerokuAssignedPort());

    staticFiles.location("static");

    before((req, res) -> acceptAuthentication(req));

    before((req, res) -> res.type("application/json"));

    post("/api/user",
        (req, res) -> toJson(UserController.registerUser(fromJson(req.body(), User.class))));

    get("/api/user/:id", (req, res) -> toJson(UserController.getUser(req.params("id"))));

    post("/api/login", (req, res) -> toJson(UserController
        .generateSession(req.queryParams("emailAddress"), req.queryParams("password"))));


    /*
     * Application endpoints
     */

    delete("/api/application", (req, res) -> requireAuthentication(req, (principle) -> toJson(ApplicationController.deleteApplication(principle, fromJson(req.body(), Application.class)))));

    post("/api/application", (req, res) -> requireAuthentication(req, (principle) -> toJson(ApplicationController.createApplication(principle, fromJson(req.body(), Application.class)))));

    get("/api/application/:id", (req, res) -> toJson(ApplicationController.getApplication(req.params(":id"))));



    get("/api/user", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(UserController.getUser(principle))));

    notFound((request, response) -> gson.toJson(new NotFound()));
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
      String[] userDetails =  uri.getUserInfo().split(":");
      String jdbcURL = String.format("jdbc:postgresql://%s:%d%s?user=%s&password=%s", uri.getHost(), uri.getPort(),
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


  private static String toJson(TransactionState transactionState) {
    return gson.toJson(transactionState);
  }

  private static <T> T fromJson(String string, Class<T> classOfT) {
    return gson.fromJson(string, classOfT);
  }

  private static String requireAuthentication(Request req, AuthentcatedRequest result) {
    if (req.attribute("isAuthenticated")) {
      return result.onAccept(req.attribute("principle"));
    } else {
      halt(401, toJson(new TransactionState<User>(null, TransactionStatus.STATUS_ERROR,
          "Could not verify authentication token")));
      return null;
    }
  }

  private static void acceptAuthentication(Request req) {
    String token = req.headers("Authorization");
    if (token == null) {
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
        halt(401, toJson(new TransactionState<User>(null, TransactionStatus.STATUS_ERROR,
            "Could not verify authentication token")));
      }
    }
  }
}