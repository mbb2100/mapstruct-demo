package com.lupf.mapstruct;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.mapper.UserMapper;
import com.lupf.mapstruct.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class MapstructDemoApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Test
    void defaultTest() {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();
        UserVO userVO = UserMapper.INSTANCE.to(userDTO);
        System.out.println(userVO);
    }

    @Test
    void springTest() {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();
        UserVO userVO = userMapper.to(userDTO);
        System.out.println(userVO);
    }

}
