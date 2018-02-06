package com.sabbreview.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sabbreview.model.Application;
import com.sabbreview.model.Department;

import java.lang.reflect.Type;
import java.util.List;

public class DepartmentAdapter implements JsonSerializer<Department> {
  @Override
  public JsonElement serialize(Department src, Type typeOfSrc, JsonSerializationContext context) {
    ApplicationAdapter applicationAdapter = new ApplicationAdapter();
    TemplateAdapter templateAdapter = new TemplateAdapter();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", src.getId());

    jsonObject.addProperty("hod", (src.getHOD().getEmailAddress() == null)?"":src.getHOD().getEmailAddress());

    List<Application> applicationList = src.getApplications();
    JsonArray applicationJsonArray = new JsonArray();
    for (Application application : applicationList) {
      applicationJsonArray.add(applicationAdapter.serialize(application, Application.class, context));
    }

    jsonObject.add("applications", applicationJsonArray);


    return null;
  }
}
