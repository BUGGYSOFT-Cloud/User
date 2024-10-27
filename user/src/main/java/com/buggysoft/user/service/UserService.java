package com.buggysoft.user.service;

import com.buggysoft.user.constants.UserType;
import com.buggysoft.user.entity.User;
import com.buggysoft.user.mapper.UserMapper;
import com.buggysoft.user.loginrequest.LoginRequest;
import java.util.List;

import com.buggysoft.user.response.AsyncResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Service
public class UserService {

  @Autowired
  UserMapper userMapper;

  private final Map<String, AsyncResponse> asyncResponses = new ConcurrentHashMap<>();


  public ResponseEntity<?> register(LoginRequest loginRequest) {
    Map<String, Object> emailMap = new HashMap<>();
    emailMap.put("email", loginRequest.getEmail());
    List<User> users = userMapper.selectByMap(emailMap);
    if (!users.isEmpty()) {
      return new ResponseEntity<>("User Already Exists!", HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>("OK, please complete the verification!", HttpStatus.OK);

  }

  public ResponseEntity<?> saveUser(LoginRequest loginRequest) {
    User newUser = new User();
    newUser.setEmail(loginRequest.getEmail());
    newUser.setPassword(loginRequest.getPassword());
    newUser.setFirstname(loginRequest.getFirstname());
    newUser.setLastname(loginRequest.getLastname());
    newUser.setGender(loginRequest.getGender());
    newUser.setUsertype(UserType.USER);
    userMapper.insert(newUser);

    URI locationUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/getUser").queryParam("email", loginRequest.getEmail()).buildAndExpand(loginRequest.getEmail()).toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(locationUri);

    return new ResponseEntity<>(newUser, headers, HttpStatus.CREATED);
  }

  public ResponseEntity<?> login(LoginRequest loginRequest) {
    System.out.println("login is called with email " + loginRequest.getEmail());
    Map<String, Object> loginMap = new HashMap<>();
    loginMap.put("email", loginRequest.getEmail());
    loginMap.put("password", loginRequest.getPassword());
    List<User> users = userMapper.selectByMap(loginMap);
    if (users.isEmpty()) {
      return new ResponseEntity<>("User or Password Incorrect", HttpStatus.BAD_REQUEST);
    }
    ;
    return new ResponseEntity<>(users.get(0), HttpStatus.OK);
  }

  public ResponseEntity<?> getAllUsers(int page, int size) {
//    if (requestingUserType != UserType.ADMIN) {
//      return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
//    }
    String requestId = UUID.randomUUID().toString();

    asyncResponses.put(requestId, new AsyncResponse("Processing", null));

    CompletableFuture.runAsync(() -> {
      listUsers(requestId, page, size);
    });

    String callbackUrl = "/listUsersStatus/" + requestId;
    return ResponseEntity
        .accepted()
        .header("Location", callbackUrl)
        .body(Map.of("requestId", requestId, "callbackUrl", callbackUrl));
  }

  public void listUsers(String requestId, int page, int size) {
    // simulate a long-running task
    // try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
    long totalRecords = userMapper.selectCount(null);
    long pageSize = (totalRecords > size) ? size : Math.min(size, totalRecords);
    int maxPage = (int) Math.ceil((double) totalRecords / pageSize);
    if (page > maxPage) {
      asyncResponses.put(requestId, new AsyncResponse("Completed",
          Map.of("message", "No Users Found", "status", HttpStatus.NO_CONTENT.value())));
      return;
    }
    List<User> users = userMapper.findAllWithPagination(pageSize, (page - 1) * pageSize);
    if (users.isEmpty()) {
      asyncResponses.put(requestId, new AsyncResponse("Completed",
          Map.of("message", "No Users Found", "status", HttpStatus.NO_CONTENT.value())));
      return;
    }

    Map<String, Object> response = new HashMap<>();
    response.put("data", users);
    String baseHref = "/getAllUsers?page=";
    Map<String, Object> links = new HashMap<>();
    links.put("current", Map.of("rel", "current", "href", baseHref + page + "&size=" + size));

    if (page > 1) {
      links.put("prev", Map.of("rel", "prev", "href", baseHref + (page - 1) + "&size=" + size));
    }
    if (page < maxPage) {
      links.put("next", Map.of("rel", "next", "href", baseHref + (page + 1) + "&size=" + size));
    }

    response.put("links", links);
    asyncResponses.put(requestId, new AsyncResponse("Completed", response));

  }

  public ResponseEntity<? extends Object> getUserStatus(String requestId) {
    AsyncResponse asyncResponse = asyncResponses.get(requestId);
    if (asyncResponse == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request ID not found.");
    }

    if ("Completed".equals(asyncResponse.getStatus())) {
      return ResponseEntity.ok(asyncResponse.getData());
    }

    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Request is still processing.");
  }

  public ResponseEntity<?> delete(LoginRequest loginRequest) {
    Map<String, Object> emailMap = new HashMap<>();
    emailMap.put("email", loginRequest.getEmail());
    List<User> users = userMapper.selectByMap(emailMap);

    if (users.isEmpty()) {
      return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
    }
    ;
    userMapper.deleteByMap(emailMap);
    return new ResponseEntity<>("Success", HttpStatus.OK);
  }

  public ResponseEntity<?> getUser(String email) {
    Map<String, Object> emailMap = new HashMap<>();
    emailMap.put("email", email);
    List<User> users = userMapper.selectByMap(emailMap);
    if (users.isEmpty()) {
      return new ResponseEntity<>("User Does Not Exist", HttpStatus.BAD_REQUEST);
    };
    return new ResponseEntity<>(users.get(0), HttpStatus.OK);
  }
}
