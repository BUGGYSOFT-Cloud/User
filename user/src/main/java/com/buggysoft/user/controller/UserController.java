package com.buggysoft.user.controller;

import com.buggysoft.user.entity.User;
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

  @PostMapping("/saveUser")
  public ResponseEntity<?> saveUser(@RequestBody LoginRequest loginRequest) {
    return userService.saveUser(loginRequest);
  }

  @PatchMapping("/delete")
  public ResponseEntity<?> deleteUser(@RequestBody LoginRequest loginRequest) {
    return userService.delete(loginRequest);
  }

  @GetMapping("getAllUsers")
  public ResponseEntity<?> getAllUsers(@RequestBody User user, @RequestParam int page, int size) {
    return userService.listUsers(page, size, user.getUsertype());
  }

  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "Welcome to user services!";
  }

  @GetMapping("/getUser")
  public ResponseEntity<?> getUserInfo(@RequestParam String email) {
    return userService.getUser(email);
  }
}
