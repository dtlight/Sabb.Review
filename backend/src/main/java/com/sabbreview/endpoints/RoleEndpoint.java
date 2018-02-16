package com.sabbreview.endpoints;

import com.sabbreview.controller.RoleController;
import com.sabbreview.model.Role;

import static spark.Spark.delete;
import static spark.Spark.post;

public class RoleEndpoint extends Endpoint {
  public static void attach() {
    delete("/role/:id", (req, res) -> requireAuthentication(req,
        (principle) -> toJson(RoleController.removeRole(principle, req.params("id")))));

    post("/role", (req, res) -> requireAuthentication(req, (principle) -> toJson(
        RoleController.createRole(principle, fromJson(req.body(), Role.class)))));
  }
}
