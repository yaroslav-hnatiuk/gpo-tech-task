package com.github.yhnatiuk.gpotechtask.service;

import com.github.yhnatiuk.gpotechtask.service.dto.UserDto;
import java.util.Map;

public interface AuthService {

  Map<String, String> login(UserDto user);

  UserDto register(UserDto user);

}
