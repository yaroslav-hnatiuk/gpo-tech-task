package com.github.yhnatiuk.gpotechtask.service;

import com.github.yhnatiuk.gpotechtask.service.dto.UserDto;
import java.util.Map;
import org.keycloak.representations.idm.UserRepresentation;

public interface AuthService {

  Map<String, String> login(UserDto user);

  UserRepresentation register(UserDto user);

}
