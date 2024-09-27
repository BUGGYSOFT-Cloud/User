package com.buggysoft.user.controller;

import com.buggysoft.user.loginrequest.LoginRequest;
import com.buggysoft.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user")
  public ResponseEntity<?> loginAndCreateUserWhenNeed(@RequestBody LoginRequest loginRequest) {
    return null;
  }
}