package com.sabbreview.endpoints;

import com.sabbreview.controller.UserController;
import com.sabbreview.model.User;

import static com.sabbreview.controller.UserController.getApplicationsForUser;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class UserEndpoint extends Endpoint {
  public static void attach() {
    delete("/user",
        (req, res) -> requireAuthentication(req, (principle) -> toJson(UserController.deleteUser(principle))));

    post("/user",
        (req, res) -> toJson(UserController.registerUser(fromJson(req.body(), User.class))));

    get("/user/by-id/:id", (req, res) -> toJson(UserController.getUser(req.params("id"))));

    post("/login", (req, res) -> toJson(UserController
        .generateSession(fromJson(req.body(), UserController.UserAuthenticationParameters.class))));

    get("/user", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(UserController.getUser(principle))));

    get("/user/applications", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(getApplicationsForUser(principle))));
  }
}
