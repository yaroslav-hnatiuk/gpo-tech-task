package com.github.yhnatiuk.gpotechtask.service.impl;

import com.github.yhnatiuk.gpotechtask.service.AuthService;
import com.github.yhnatiuk.gpotechtask.service.dto.Credentials;
import com.github.yhnatiuk.gpotechtask.service.dto.UserDto;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  private String tokenUrl = "http://localhost:8484/auth/realms/my_realm/protocol/openid-connect/token";

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
  public UserDto register(UserDto user) {
    String realmAdminToken = getRealmAdminToken();
    UserDto registeredUser = registerNewUser(user, realmAdminToken);
    return null;
  }

  private UserDto registerNewUser(UserDto user, String realmAdminToken) {
    String registerUrl = "http://localhost:8484/auth/admin/realms/my_realm/users";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + realmAdminToken);
    HttpEntity<UserDto> userDtoHttpEntity = new HttpEntity<>(user, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserDto> aaa = restTemplate.postForEntity(URI.create(registerUrl),
        userDtoHttpEntity, UserDto.class);
    String userId = getUserIdByEmailAndUserName(user.getEmail(), user.getUsername(),
        realmAdminToken);
    return null;
  }

  private String getUserIdByEmailAndUserName(String email, String username,
      String realmAdminToken) {
    var url = String.format(
        "http://localhost:8484/auth/admin/realms/my_realm/users?email=%s&username=%s&exect=true",
        email,
        username);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + realmAdminToken);
    HttpEntity<UserDto> userDtoHttpEntity = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<UserDto> aaa = restTemplate.exchange(url, HttpMethod.GET, userDtoHttpEntity,
        UserDto.class);

    return null;
  }

  private String getRealmAdminToken() {
    UserDto admin = new UserDto();
    List<Credentials> credentialsList = new ArrayList<>();
    credentialsList.add(new Credentials());
    admin.setCredentials(credentialsList);
    admin.setUsername("admin");
    admin.getCredentials().get(0).setValue("admin");
    return login(admin).get("token");
  }
}
