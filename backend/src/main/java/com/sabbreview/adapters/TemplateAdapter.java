package com.sabbreview.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sabbreview.model.Field;
import com.sabbreview.model.Template;

import java.lang.reflect.Type;
import java.util.List;

public class TemplateAdapter implements JsonSerializer<Template> {
  @Override
  public JsonElement serialize(Template src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject templateJSON = new JsonObject();
    templateJSON.addProperty("id", src.getId());
    templateJSON.addProperty("name", src.getName());

    List<Field> fieldList = src.getFieldList();
    if(fieldList != null){
      JsonArray fieldArray = new JsonArray();
      for (Field field:
          fieldList) {
        fieldArray.add(context.serialize(field));
      }
      templateJSON.add("fields", fieldArray);
    }

    //templateJSON.addProperty("department", src.getDepartment().getId());
    return templateJSON;
  }

/*  @Override
  public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject user = new JsonObject();
    user.addProperty("emailAddress", src.getEmailAddress());
    user.addProperty("isAdmin", (src.getAdmin() == null)?false:src.getAdmin());

    JsonArray applicationsJsonArray =  new JsonArray();
    for (ApplicationEndpoint application:
         src.getApplications()) {
      System.out.println(application);

      applicationsJsonArray.add(application.getId());
    }
    user.add("applications", applicationsJsonArray);


    JsonArray assignmentsJsonArray =  new JsonArray();
    for (Assignment assignment:
        src.getAssignments()) {
      System.out.println(assignment);
      assignmentsJsonArray.add(assignment.getId());
    }
    user.add("assignments", assignmentsJsonArray);

    return user;
  }*/
}
