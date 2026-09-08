package com.example.billdemo.mapper;


import com.example.billdemo.entity.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {


    // 根据用户名查询用户（登录使用）
    @Select("""
            select 
            id,
            username,
            password,
            nickname,
            create_time as createTime
            from users
            where username=#{username}
            """)
    User findByUsername(String username);



    // 注册用户
    @Insert("""
            insert into users(
            username,
            password,
            nickname,
            create_time
            )
            values(
            #{username},
            #{password},
            #{nickname},
            now()
            )
            """)
    void register(User user);


    @Select("select * from users where id=#{id}")
    User findById(Integer id);


}