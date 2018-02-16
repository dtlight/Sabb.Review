package com.sabbreview.responses;

public class ValidationException extends Exception {
  String validationField;

  public ValidationException() {

  }

  public ValidationException(String field) {
    this.validationField = field;
  }

  public String getValidationField() {
    return validationField;
  }

  public ValidationException setValidationField(String validationField) {
    this.validationField = validationField;
    return this;
  }

  @Override public String getMessage() {
    return "Error in field: " + validationField;
  }
}
