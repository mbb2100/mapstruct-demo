package com.lupf.mapstruct.test;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.mapper.User4Mapper;
import com.lupf.mapstruct.vo.UserVO1;

import java.util.Date;


/**
 * 忽略指定字段的映射
 */
public class t4 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();

        UserVO1 userVO1 = User4Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO1);
    }
}
