package com.sabbreview.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sabbreview.model.Application;
import com.sabbreview.model.Department;
import com.sabbreview.model.Template;

import java.lang.reflect.Type;
import java.util.List;

public class DepartmentAdapter implements JsonSerializer<Department> {
  @Override
  public JsonElement serialize(Department src, Type typeOfSrc, JsonSerializationContext context) {
  JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", src.getId());

    jsonObject.addProperty("hod", (src.getHOD().getEmailAddress() == null)?"":src.getHOD().getEmailAddress());

    List<Application> applicationList = src.getApplications();
    JsonArray applicationJsonArray = new JsonArray();
    for (Application application : applicationList) {
      applicationJsonArray.add(context.serialize(application));
    }

    List<Template> templateList = src.getTemplateList();
    JsonArray templateJsonElements = new JsonArray();
    for (Template template : templateList) {
      templateJsonElements.add(context.serialize(template));
    }

    jsonObject.add("applications", applicationJsonArray);
    jsonObject.add("templates", templateJsonElements);


    return jsonObject;
  }
}
