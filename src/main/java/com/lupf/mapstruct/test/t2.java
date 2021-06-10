package com.lupf.mapstruct.test;

import com.lupf.mapstruct.dto.AddressDTO;
import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.mapper.User2Mapper;
import com.lupf.mapstruct.vo.UserVO2;

import java.util.Date;

/**
 * 多个源对象映射到一个
 */
public class t2 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();

        AddressDTO addressDTO = AddressDTO.builder()
                .country("中国")
                .province("北京")
                .city("北京")
                .build();
        UserVO2 userVO2 = User2Mapper.INSTANCE.to(userDTO, addressDTO);
        System.out.println(userVO2);
    }
}
