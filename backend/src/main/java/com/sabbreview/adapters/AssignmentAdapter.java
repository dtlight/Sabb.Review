package com.sabbreview.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sabbreview.model.Assignment;

import java.lang.reflect.Type;

public class AssignmentAdapter implements JsonSerializer<Assignment> {

  @Override
  public JsonElement serialize(Assignment src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", src.getId());
    jsonObject.addProperty("assignee", src.getAssignee().getEmailAddress());

    jsonObject.addProperty("state", src.getState().name());
    jsonObject.add("application", context.serialize(src.getApplication()));
    jsonObject.add("comments", context.serialize(src.getComments()));

    //jsonObject.add("applicant", );*/

    return jsonObject;
  }
}
