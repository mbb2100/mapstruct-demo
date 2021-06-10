package com.lupf.mapstruct.test;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.mapper.User3Mapper;
import com.lupf.mapstruct.vo.UserVO3;

import java.util.Date;

/**
 * 不同属性名之间的映射
 */
public class t3 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();

        UserVO3 userVO3 = User3Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO3);

        UserDTO userDTO1 = User3Mapper.INSTANCE.from(userVO3);
        System.out.println(userDTO1);
    }
}
