package com.buggysoft.user.controller;

import com.buggysoft.user.loginrequest.LoginRequest;
import com.buggysoft.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody LoginRequest loginRequest) {
    return userService.register(loginRequest);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
    return userService.login(loginRequest);
  }

  @PostMapping
  public ResponseEntity<?> saveUser(@RequestBody LoginRequest loginRequest) {
    return userService.saveUser(loginRequest);
  }

  @PatchMapping("/delete")
  public ResponseEntity<?> deleteUser(@RequestBody LoginRequest loginRequest) {
    return userService.delete(loginRequest);
  }

  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "Welcome to user services!";
  }
}