package com.sabbreview.endpoints;

import com.sabbreview.model.Field;
import com.sabbreview.model.FieldOption;

import static com.sabbreview.controller.FieldController.addOption;
import static com.sabbreview.controller.FieldController.createField;
import static com.sabbreview.controller.FieldController.deleteField;
import static com.sabbreview.controller.FieldController.editField;
import static com.sabbreview.controller.FieldController.getField;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class FieldEndpoint extends Endpoint {
  public static void attach() {
    post("/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createField(principle, fromJson(req.body(), Field.class))))));
    put("/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(editField(principle, fromJson(req.body(), Field.class))))));
    post("/field/:id/option", (req, res) -> requireAuthentication(req,
        (principle -> toJson(addOption(principle, req.params(":id"), fromJson(req.body(), FieldOption.class))))));
    delete("/field/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(deleteField(principle, req.params(":id"))))));
    get("/field/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(getField(principle, req.params(":id"))))));
    get("/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(getField(principle, req.params(":id"))))));
  }
}
