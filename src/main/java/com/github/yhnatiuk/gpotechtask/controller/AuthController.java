package com.github.yhnatiuk.gpotechtask.controller;

import com.github.yhnatiuk.gpotechtask.service.AuthService;
import com.github.yhnatiuk.gpotechtask.service.dto.UserDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(path = "/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody UserDto user){
    return ResponseEntity.ok(authService.login(user));
  }

  @GetMapping(path = "/logout")
  public ResponseEntity<String> logout(){
    return null;
  }

}
