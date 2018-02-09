package com.sabbreview.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.User;

import java.lang.reflect.Type;

public class UserAdadpter implements JsonSerializer<User> {

  @Override
  public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject user = new JsonObject();
    user.addProperty("emailAddress", src.getEmailAddress());
    user.addProperty("isAdmin", (src.getAdmin() == null)?false:src.getAdmin());

    JsonArray applicationsJsonArray =  new JsonArray();


    for (Application application:
         src.getApplications()) {
      applicationsJsonArray.add(context.serialize(application));
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
  }
}
