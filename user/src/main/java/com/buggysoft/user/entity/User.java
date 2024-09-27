package com.buggysoft.user.entity;

import com.buggysoft.user.constants.Gender;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class User {
  private String email;
  private String password;
  private String name;
  private Gender gender;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
