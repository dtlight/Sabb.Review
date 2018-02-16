package com.sabbreview.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sabbreview.model.Field;

import java.lang.reflect.Type;

public class FieldAdapter  implements JsonSerializer<Field> {

  @Override
  public JsonElement serialize(Field src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject object = new JsonObject();
    object.addProperty("id", src.getId());
    object.addProperty("title", src.getTitle());
    object.addProperty("type", src.getType().name());
    object.add("fieldOptions", context.serialize(src.getFieldOptions()));

    return object;
  }
}
