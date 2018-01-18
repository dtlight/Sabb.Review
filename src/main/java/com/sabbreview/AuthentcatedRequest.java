package com.sabbreview;

@FunctionalInterface public interface AuthentcatedRequest {
  String onAccept(String principle);
}
