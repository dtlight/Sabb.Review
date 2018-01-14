package com.sabbreview;

import com.sabbreview.responses.HelloWorld;
import com.sabbreview.responses.NotFound;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class SabbReview {

  public static void main(String... args) {
    port(getHerokuAssignedPort());

    staticFiles.location("static");

    before((req, res) -> {
      res.type("application/json");
    });

    get("/test", (req, res) -> new HelloWorld().toJSON());

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
