package com.sabbreview.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sabbreview.model.Application;
import com.sabbreview.model.Field;
import com.sabbreview.model.FieldInstance;

import java.lang.reflect.Type;

public class ApplicationAdapter implements JsonSerializer<Application> {

  @Override
  public JsonElement serialize(Application src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", src.getId());
    jsonObject.addProperty("state", src.getState().name());
    jsonObject.addProperty("department", src.getDepartment().getName());

    JsonArray fields = new JsonArray();

    for (FieldInstance fieldInstance : src.getFields()) {
      Field field = fieldInstance.getField();

      JsonObject fieldInstanceObject = new JsonObject();
      fieldInstanceObject.addProperty("id", fieldInstance.getId());
      fieldInstanceObject.addProperty("value", fieldInstance.getValue());
      fieldInstanceObject.add("field", context.serialize(field));
      fieldInstanceObject.add("selected", context.serialize(fieldInstance.getSelected()));

      fields.add(fieldInstanceObject);
    }

    jsonObject.add("fields", fields);

    return jsonObject;
  }
}
