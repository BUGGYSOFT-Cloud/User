package com.buggysoft.user.controller;

import com.buggysoft.user.constants.UserType;
import com.buggysoft.user.entity.User;
import com.buggysoft.user.loginrequest.LoginRequest;
import com.buggysoft.user.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping
@Tag(name = "User API", description = "User management operations")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  @Operation(summary = "Register User", description = "Registers a user in the system.")
  @ApiResponse(
      responseCode = "200",
      description = "User registration initiated successfully",
      content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "User registered successfully. Please complete the verification process."))
  )
  @ApiResponse(
      responseCode = "400",
      description = "User already exists",
      content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Registration failed: User already exists."))
  )
  public ResponseEntity<?> registerUser(@RequestBody LoginRequest loginRequest) {
    return userService.register(loginRequest);
  }

  @PostMapping("/login")
  @Operation(summary = "Login", description = "Logs in with credentials.")
  @ApiResponse(
      responseCode = "200",
      description = "Successful login",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
  )
  @ApiResponse(
      responseCode = "400",
      description = "Invalid user and/or password",
      content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Login failed: Invalid username or password."))
  )
  public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
    return userService.login(loginRequest);
  }

  @PostMapping("/saveUser")
  @Operation(summary = "Save User", description = "Saves a new user in the system with the provided details.")
  @ApiResponse(
      responseCode = "201",
      description = "User created successfully",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
  )
  public ResponseEntity<?> saveUser(@RequestBody LoginRequest loginRequest, @RequestParam String userType) {
    if (userService.isUserRegistered(loginRequest.getEmail())) {
      return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
    }
    return userService.saveUser(loginRequest, userType);
  }

  @PatchMapping("/delete")
  @Operation(summary = "Delete User", description = "Deletes a user based on email.")
  @ApiResponse(
      responseCode = "200",
      description = "User deleted successfully",
      content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "User deleted successfully."))
  )
  @ApiResponse(
      responseCode = "400",
      description = "User not found",
      content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Deletion failed: User not found."))
  )
  public ResponseEntity<?> deleteUser(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
    String email = (String) request.getAttribute("email");
    if (!email.equals(loginRequest.getEmail()) || userService.getUserByEmail(loginRequest.getEmail()).getUserType() != UserType.ADMIN) {
      return new ResponseEntity<>("Insufficient Permission.", HttpStatus.FORBIDDEN);
    }
    return userService.delete(loginRequest);
  }

  @GetMapping("/getAllUsers")
  @Operation(summary = "Get All Users (Async)", description = "Retrieves all users asynchronously with pagination.")
  @ApiResponse(
      responseCode = "202",
      description = "Request accepted for asynchronous processing",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(value = "{ \"requestId\": \"123e4567-e89b-12d3-a456-426614174000\", \"callbackUrl\": \"/listUsersStatus/123e4567-e89b-12d3-a456-426614174000\" }")
      )
  )
  public ResponseEntity<?> getAllUsers(HttpServletRequest request, @RequestParam int page, @RequestParam int size) {
    String email = (String) request.getAttribute("email");
    if (userService.getUserByEmail(email).getUserType() != UserType.ADMIN) {
      return new ResponseEntity<>("Insufficient Permission.", HttpStatus.FORBIDDEN);
    }
    return userService.getAllUsersAsync(page, size);
  }

  @GetMapping("/getAllUserSync")
  @Operation(summary = "Get All Users (Sync)", description = "Retrieves all users synchronously with pagination.")
  @ApiResponse(
      responseCode = "202",
      description = "Users retrieved successfully",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(value = "{ \"status\": \"Completed\", \"data\": [{\"id\": 1, \"email\": \"user@example.com\", \"name\": \"John Doe\"}] }")
      )
  )
  @ApiResponse(
      responseCode = "500",
      description = "Failed to complete the request",
      content = @Content(
          mediaType = "text/plain",
          examples = @ExampleObject(value = "Request failed: Unable to retrieve users.")
      )
  )
  public ResponseEntity<?> getAllUsersSync(HttpServletRequest request, @RequestParam int page, @RequestParam int size) {
    String email = (String) request.getAttribute("email");
    if (userService.getUserByEmail(email).getUserType() != UserType.ADMIN) {
      return new ResponseEntity<>("Insufficient Permission.", HttpStatus.FORBIDDEN);
    }
    return userService.getAllUsersSync(page, size);
  }

  @GetMapping("/listUsersStatus/{requestId}")
  @Operation(summary = "Get Async Request Status", description = "Retrieves the status of an asynchronous user retrieval request.")
  @ApiResponse(
      responseCode = "200",
      description = "Request completed successfully with user data",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(value = "{ \"status\": \"Completed\", \"data\": [{\"id\": 1, \"email\": \"user@example.com\", \"name\": \"John Doe\"}], \"links\": { \"current\": { \"rel\": \"current\", \"href\": \"/getAllUsers?page=1&size=10\" }, \"prev\": { \"rel\": \"prev\", \"href\": \"/getAllUsers?page=0&size=10\" }, \"next\": { \"rel\": \"next\", \"href\": \"/getAllUsers?page=2&size=10\" } } }")
      )
  )
  @ApiResponse(
      responseCode = "202",
      description = "Request is still processing",
      content = @Content(
          mediaType = "text/plain",
          examples = @ExampleObject(value = "Request is still processing.")
      )
  )
  @ApiResponse(
      responseCode = "404",
      description = "Request ID not found",
      content = @Content(
          mediaType = "text/plain",
          examples = @ExampleObject(value = "Error: Request ID not found.")
      )
  )
  public ResponseEntity<?> getStatus(@PathVariable String requestId) {
    return userService.getUserStatus(requestId);
  }

  @GetMapping({"/", "/index", "/home"})
  @Operation(summary = "Welcome Page", description = "Provides a welcome message for the User API.")
  @ApiResponse(
      responseCode = "200",
      description = "Welcome message displayed",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(value = "{\"message\": \"Welcome to user services!\"}")
      )
  )
  public ResponseEntity<EntityModel<Map<String, String>>> index() {
    Map<String, String> welcomeMessage = Map.of(
        "message", "Welcome to user services!",
        "registerDetails", "POST to /register with JSON body: { 'email': 'your_email', 'password': 'your_password', 'firstname': 'your_firstname', 'lastname': 'your_lastname', 'gender': your_gender}",
        "loginDetails", "POST to /login with JSON body: { 'email': 'your_email', 'password': 'your_password' }"
    );

    Link registerLink = Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
            .registerUser(null)).toUri().toString())
        .withRel("register")
        .withTitle("Register a New User (JSON body: email, password, firstname, lastname, gender)")
        .withType("POST");

    Link loginLink = Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
            .loginUser(null)).toUri().toString())
        .withRel("login")
        .withTitle("Login (JSON body: email, password)")
        .withType("POST");

    EntityModel<Map<String, String>> responseModel = EntityModel.of(welcomeMessage, registerLink, loginLink);

    return new ResponseEntity<>(responseModel, HttpStatus.OK);
  }

  @GetMapping("/getUser")
  @Operation(summary = "Get User Information", description = "Retrieves information of a specific user by email.")
  @ApiResponse(
      responseCode = "200",
      description = "User found",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = User.class)
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "User does not exist",
      content = @Content(
          mediaType = "text/plain",
          examples = @ExampleObject(value = "User lookup failed: User does not exist.")
      )
  )
  public ResponseEntity<?> getUserInfo(@RequestParam String email) {
    return userService.getUser(email);
  }

}
