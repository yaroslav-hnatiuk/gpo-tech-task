package com.github.yhnatiuk.gpotechtask.service.dto;

public class Credentials {
  private Type type;
  private String value;
  private boolean temporary;

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isTemporary() {
    return temporary;
  }

  public void setTemporary(boolean temporary) {
    this.temporary = temporary;
  }
}
