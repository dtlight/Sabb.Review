package com.sabbreview;

import com.google.gson.Gson;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.Role;
import com.sabbreview.model.User;
import com.sabbreview.responses.HelloWorld;
import com.sabbreview.responses.NotFound;

import java.net.URI;
import java.util.HashMap;
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
  private static final String DB_ENV_VARIABLE = "POSTGRES_ELEPHANT";

  public static void main(String... args) {
    EntityManager em = getEntityManager();

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

    get("/api/user/:id", (req, res) -> new Gson().toJson(em.find(User.class, req.params("id"))));

    em.getTransaction().begin();
    User testUser = new User("matthew@bargrove.com", "testpw");
    User testUser2 = new User("geoff@bargrove.com", "testpw2");
    em.persist(testUser);
    em.persist(testUser2);
    Application testApplication = new Application(testUser);
    em.persist(testApplication);
    Role role = new Role("Test Role");
    em.persist(role);
    Assignment assignment = new Assignment(testUser2, testApplication, role);
    testUser.addAssignment(assignment);
    em.persist(assignment);
    em.getTransaction().commit();


    notFound((request, response) -> new NotFound().toJSON());
  }

  private static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567;
  }

  private static EntityManager getEntityManager() {
    EntityManagerFactory entityManagerFactory;
    if(System.getenv(DB_ENV_VARIABLE) != null){
      URI uri = URI.create(System.getenv(DB_ENV_VARIABLE));
      HashMap<String, String> persistenceMap = new HashMap<>();
      persistenceMap.put("javax.persistence.jdbc.url", "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath()+"?sslmode=require");
      persistenceMap.put("javax.persistence.jdbc.user", uri.getUserInfo().split(":")[0]);
      persistenceMap.put("javax.persistence.jdbc.password", uri.getUserInfo().split(":")[1]);
      persistenceMap.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");

      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, persistenceMap);
    } else {
      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    return entityManagerFactory.createEntityManager();
  }
}
