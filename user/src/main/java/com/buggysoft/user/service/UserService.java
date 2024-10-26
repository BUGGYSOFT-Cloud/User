package com.buggysoft.user.service;

import com.buggysoft.user.constants.UserType;
import com.buggysoft.user.entity.User;
import com.buggysoft.user.mapper.UserMapper;
import com.buggysoft.user.loginrequest.LoginRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

@Service
public class UserService {

  @Autowired
  UserMapper userMapper;

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

  public ResponseEntity<?> listUsers(int page, int size) {
//    if (requestingUserType != UserType.ADMIN) {
//      return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
//    }
    long totalRecords = userMapper.selectCount(null);
    long pageSize = (totalRecords > size) ? size : Math.min(size, totalRecords);
    int maxPage = (int) Math.ceil((double) totalRecords / pageSize);
    if (page > maxPage) {
      return new ResponseEntity<>("No Users Found", HttpStatus.NO_CONTENT);
    }
    List<User> users = userMapper.findAllWithPagination(pageSize, (page - 1) * pageSize);
    if (users.isEmpty()) {
      return new ResponseEntity<>("No Users Found", HttpStatus.NO_CONTENT);
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

    return new ResponseEntity<>(response, HttpStatus.OK);
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
