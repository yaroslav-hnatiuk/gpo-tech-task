package com.github.yhnatiuk.gpotechtask.service.impl;

import com.github.yhnatiuk.gpotechtask.service.AuthService;
import com.github.yhnatiuk.gpotechtask.service.dto.Credentials;
import com.github.yhnatiuk.gpotechtask.service.dto.Role;
import com.github.yhnatiuk.gpotechtask.service.dto.UserDto;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KeyCloakAuthServiceImpl implements AuthService {

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.url-login}")
  private String keycloakUrlLogin;

  @Value("${keycloak.url-admin}")
  private String keycloakUrlAdmin;

  @Value("${keycloak.username}")
  private String username;

  @Value("${keycloak.password}")
  private String password;

  @Override
  public Map<String, String> login(UserDto user) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", "my_client");
    map.add("username", user.getUsername());
    map.add("password", user.getCredentials().get(0).getValue());
    map.add("grant_type", "password");
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    try {
      String tokenUrl = String.format(keycloakUrlLogin + "/protocol/openid-connect/token", realm);
      ResponseEntity<KeycloakAuthResponse> response = restTemplate.postForEntity(
          tokenUrl, request, KeycloakAuthResponse.class);
      Map<String, String> res = new HashMap<>();
      res.put("token", response.getBody().getAccessToken());
      return res;
    } catch (HttpClientErrorException ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Username or password is not correct.");
    }
  }

  @Override
  public UserRepresentation register(UserDto user) {
    String realmAdminToken = getRealmAdminToken();
    return registerNewUser(user, realmAdminToken);
  }

  private UserRepresentation registerNewUser(UserDto user, String realmAdminToken) {
    String registerUrl = String.format(keycloakUrlAdmin, realm);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + realmAdminToken);
    HttpEntity<UserDto> userDtoHttpEntity = new HttpEntity<>(user, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserRepresentation> response = restTemplate.postForEntity(
        URI.create(registerUrl),
        userDtoHttpEntity, UserRepresentation.class);
    if (response.getStatusCode().equals(HttpStatus.CREATED)) {
      UserRepresentation userRepresentation =
          getUserIdByEmailAndUserName(user.getEmail(), user.getUsername(), realmAdminToken);
      setRoleToTheNewUser(userRepresentation, realmAdminToken);
      return getUserIdByEmailAndUserName(user.getEmail(), user.getUsername(), realmAdminToken);
    } else {
      throw new RuntimeException("User was not created.");
    }
  }

  private void setRoleToTheNewUser(UserRepresentation user, String realmAdminToken) {
    /*
     * Add realm-level role mappings to the user
     * POST /{realm}/users/{id}/role-mappings/realm
     */
    String setRoleUrl = String.format(keycloakUrlAdmin + "/%s/role-mappings/realm", realm, user.getId());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + realmAdminToken);
    List<RoleRepresentation> roles = getRolesThatCanBeMapped(user, realmAdminToken);
    RoleRepresentation newUserRole = getRole(roles, Role.USER);
    HttpEntity<List<RoleRepresentation>> userDtoHttpEntity = new HttpEntity<>(List.of(newUserRole), headers);
    new RestTemplate().postForEntity(URI.create(setRoleUrl), userDtoHttpEntity, String.class);
  }

  private RoleRepresentation getRole(List<RoleRepresentation> roles, Role role) {
    return roles.stream()
        .filter(r -> r.getName().equals(role.name()))
        .findAny()
        .orElse(null);
  }

  private List<RoleRepresentation> getRolesThatCanBeMapped(UserRepresentation userRepresentation,
      String realmAdminToken) {
    String urlGetRoles = String.format(
        keycloakUrlAdmin + "/%s/role-mappings/realm/available", realm,
        userRepresentation.getId());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + realmAdminToken);
    HttpEntity<UserDto> userDtoHttpEntity = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<RoleRepresentation[]> response = restTemplate.exchange(urlGetRoles,
        HttpMethod.GET,
        userDtoHttpEntity,
        RoleRepresentation[].class);
    return List.of(response.getBody());
  }

  private UserRepresentation getUserIdByEmailAndUserName(String email, String username,
      String realmAdminToken) {
    var url = String.format(keycloakUrlAdmin + "?email=%s&username=%s&exect=true",
        realm,
        email,
        username);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + realmAdminToken);
    HttpEntity<UserDto> userDtoHttpEntity = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserRepresentation[]> response = restTemplate.exchange(url, HttpMethod.GET,
        userDtoHttpEntity,
        UserRepresentation[].class);
    return response.getBody()[0];
  }

  private String getRealmAdminToken() {
    UserDto admin = new UserDto();
    List<Credentials> credentialsList = new ArrayList<>();
    credentialsList.add(new Credentials());
    admin.setCredentials(credentialsList);
    admin.setUsername(username);
    admin.getCredentials().get(0).setValue(password);
    return login(admin).get("token");
  }
}
