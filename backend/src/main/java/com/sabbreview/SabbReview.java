package com.sabbreview;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sabbreview.adapters.ApplicationAdapter;
import com.sabbreview.adapters.FieldAdapter;
import com.sabbreview.adapters.TemplateAdapter;
import com.sabbreview.adapters.UserAdadpter;
import com.sabbreview.endpoints.ApplicationEndpoint;
import com.sabbreview.endpoints.AssignmentEndpoint;
import com.sabbreview.endpoints.DepartmentEndpoint;
import com.sabbreview.endpoints.FieldEndpoint;
import com.sabbreview.endpoints.RoleEndpoint;
import com.sabbreview.endpoints.TemplateEndpoint;
import com.sabbreview.endpoints.UserEndpoint;
import com.sabbreview.model.Application;
import com.sabbreview.model.Field;
import com.sabbreview.model.Template;
import com.sabbreview.model.User;
import com.sabbreview.responses.NotFound;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import spark.Request;

import static spark.Spark.before;
import static spark.Spark.halt;
import static spark.Spark.notFound;
import static spark.Spark.options;
import static spark.Spark.port;

public class SabbReview {
  public static Gson gson = generateGson();

  public static void main(String... args) {
    port(getHerokuAssignedPort());

    before((req, res) -> {
      acceptAuthentication(req);
      res.type("application/json");
      res.header("Access-Control-Allow-Origin", "*");
      res.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");
      res.header("Access-Control-Allow-Headers", "Content-Type, Referer, Origin, User-Agent, Accept, Authorization");
    });

    ApplicationEndpoint.attach();
    DepartmentEndpoint.attach();
    UserEndpoint.attach();
    RoleEndpoint.attach();
    FieldEndpoint.attach();
    TemplateEndpoint.attach();
    AssignmentEndpoint.attach();
    //PDFGeneratorController.attach();

    options("*", (req, res) -> "");

    notFound((request, response) -> gson.toJson(new NotFound()));
  }

  private static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567;
  }


  private static Gson generateGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(User.class, new UserAdadpter());
    gsonBuilder.registerTypeAdapter(Template.class, new TemplateAdapter());
    gsonBuilder.registerTypeAdapter(Application.class, new ApplicationAdapter());
    gsonBuilder.registerTypeAdapter(Field.class, new FieldAdapter());
    return gsonBuilder.create();
  }



  private static void acceptAuthentication(Request req) {
    String token = req.headers("Authorization");
    if (token == null || !token.contains(".")) {
      req.attribute("isAuthenticated", false);
    } else {
      try {
        String jwtString = token.split("Bearer ")[1];
        Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECURE_KEY"));
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
