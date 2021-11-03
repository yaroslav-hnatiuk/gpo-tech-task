package com.github.yhnatiuk.gpotechtask.service.dto;

public enum Type {
  PASSWORD("password");
  private String value;

  Type(String value) {
    this.value = value;
  }

  public String getValue(){
    return value;
  }
}
