package com.sabbreview;

import com.google.gson.Gson;
import com.sabbreview.model.User;
import com.sabbreview.responses.HelloWorld;
import com.sabbreview.responses.NotFound;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class SabbReview {
  private static final String PERSISTENCE_UNIT_NAME = "SabbReview";
  private static EntityManagerFactory factory;

  public static void main(String... args) {
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();

    port(getHerokuAssignedPort());

    staticFiles.location("static");

    before((req, res) -> {
      res.type("application/json");
    });

    get("/test", (req, res) -> new HelloWorld().toJSON());

    post("/api/user/register", (req, res) -> {
      em.getTransaction().begin();
      User user = new User(req.queryParams("username"), req.queryParams("password"));
      em.persist(user);
      em.getTransaction().commit();
      return new Gson().toJson(user);
    });


    notFound((request, response) -> new NotFound().toJSON());
  }

  private static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567;
  }

}
