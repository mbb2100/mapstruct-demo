package com.lupf.mapstruct.test;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.mapper.User5Mapper;
import com.lupf.mapstruct.mapper.User6Mapper;
import com.lupf.mapstruct.vo.UserVO1;

import java.util.Date;


/**
 * 多个自定义的格式作用于不同的属性
 */
public class t6 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        UserVO1 userVO1 = User6Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO1);

    }
}
