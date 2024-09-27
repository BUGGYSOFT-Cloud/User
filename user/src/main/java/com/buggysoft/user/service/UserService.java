package com.buggysoft.user.service;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
public class UserService {

  public ResponseEntity<?> register(String email)
  {
    // TODO: add to SQL
    return new ResponseEntity<>("", HttpStatus.OK);
  }
  public ResponseEntity<?> login(String email)
  {
    // TODO: if not exist --> return new ResponseEntity<>("User does not exist", HttpStatus.NotFound);
    return new ResponseEntity<>("", HttpStatus.OK);
  }

  public ResponseEntity<?> getUser(String email) {
    // TODO: if not exist --> return new ResponseEntity<>("User does not exist", HttpStatus.NotFound);
    // TODO: return user info
    return new ResponseEntity<>("", HttpStatus.OK);
  }



}
