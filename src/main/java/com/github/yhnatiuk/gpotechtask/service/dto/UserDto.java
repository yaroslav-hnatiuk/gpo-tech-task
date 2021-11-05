package com.github.yhnatiuk.gpotechtask.service.dto;

import java.util.List;

public class UserDto {

  private String id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private boolean enabled;
  private boolean emailVerified;
  private List<Credentials> credentials;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public List<Credentials> getCredentials() {
    return credentials;
  }

  public void setCredentials(List<Credentials> credentials) {
    this.credentials = credentials;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
