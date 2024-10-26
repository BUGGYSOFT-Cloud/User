package com.buggysoft.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buggysoft.user.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {
  @Select("SELECT * FROM users LIMIT #{limit} OFFSET #{offset}")
  List<User> findAllWithPagination(@Param("limit") long limit, @Param("offset") long offset);
}
