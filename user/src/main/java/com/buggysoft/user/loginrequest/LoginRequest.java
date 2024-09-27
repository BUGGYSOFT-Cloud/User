package com.buggysoft.user.loginrequest;

import com.buggysoft.user.constants.Gender;

import lombok.Data;
@Data
public class LoginRequest {
  private String email;
  private String password;
  private String firstname;
  private String lastname;
  private Gender gender;
}
