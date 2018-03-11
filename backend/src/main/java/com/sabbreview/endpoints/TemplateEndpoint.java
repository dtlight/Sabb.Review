package com.sabbreview.endpoints;

import com.sabbreview.model.Field;

import static com.sabbreview.controller.TemplateController.addField;
import static com.sabbreview.controller.TemplateController.createTemplate;
import static com.sabbreview.controller.TemplateController.deleteTemplate;
import static com.sabbreview.controller.TemplateController.deleteTemplateField;
import static com.sabbreview.controller.TemplateController.getTemplate;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class TemplateEndpoint extends Endpoint {

  public static void attach() {
    post("/template/:name/department/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(createTemplate(principle, req.params("name"), req.params("id"))))));

    get("/template/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(getTemplate(principle, req.params(":id"))))));

    post("/template/:id/field", (req, res) -> requireAuthentication(req,
        (principle -> toJson(addField(principle, req.params("id"), fromJson(req.body(), Field.class))))));

    delete("/template/:id", (req, res) -> requireAuthentication(req,
        (principle -> toJson(deleteTemplate(principle, req.params(":id"))))));

    delete("/template/:id/field/:fieldid", (req, res) -> requireAuthentication(req,
        (principle -> toJson(deleteTemplateField(principle, req.params(":id"), req.params(":fieldid"))))));

  }
}
