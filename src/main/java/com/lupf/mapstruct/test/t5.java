package com.lupf.mapstruct.test;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.mapper.User5Mapper;
import com.lupf.mapstruct.vo.UserVO1;

import java.util.Date;


/**
 * 自定义转换格式
 */
public class t5 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        UserVO1 userVO1 = User5Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO1);
    }
}
