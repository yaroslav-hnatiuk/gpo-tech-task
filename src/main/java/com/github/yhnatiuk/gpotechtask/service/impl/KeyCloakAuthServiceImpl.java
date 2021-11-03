package com.github.yhnatiuk.gpotechtask.service.impl;

import com.github.yhnatiuk.gpotechtask.service.AuthService;
import com.github.yhnatiuk.gpotechtask.service.dto.UserDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    map.add("password", user.getCredentials().getValue());
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
  public Object register(UserDto user) {
    String realAdminToken = getRealmAdminToken();
    return null;
  }

  private String getRealmAdminToken() {
    UserDto admin = new UserDto();
    admin.setUsername("admin");
    admin.getCredentials().setValue("admin");
    return login(admin).get("token");
  }
}
