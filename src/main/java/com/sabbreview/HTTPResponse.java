package com.sabbreview;

import com.google.gson.Gson;

public abstract class HTTPResponse {
  public String toJSON() {
   return new Gson().toJson(this);
  }
}
