package com.buggysoft.user.usermapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buggysoft.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapperTest extends BaseMapper<User> {
}
