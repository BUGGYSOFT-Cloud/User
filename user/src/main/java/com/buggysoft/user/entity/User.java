package com.buggysoft.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.buggysoft.user.constants.Gender;
import com.buggysoft.user.constants.UserType;
import lombok.Data;

@Data
@TableName("users")
public class User {
  @TableId
  private String email;
  private String password;
  private String firstname;
  private String lastname;
  private Gender gender;
  private UserType userType;
}
