package com.lupf.mapstruct.test;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.mapper.User1Mapper;
import com.lupf.mapstruct.vo.UserVO;
import com.lupf.mapstruct.vo.UserVO1;
import org.springframework.beans.BeanUtils;

/**
 * BeanUtils与Mapstruct性能对比
 */
public class t10 {
    public static void main(String[] args) {
        for (int j = 0; j < 10; j++) {
            Long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                UserDTO userDTO = UserDTO.builder()
                        .name("张三")
                        .age(10)
                        .build();

                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userDTO, userVO);
            }
            System.out.println("BeanUtils 100W次转换耗时:" + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                UserDTO userDTO = UserDTO.builder()
                        .name("张三")
                        .age(10)
                        .build();

                UserVO1 userVO1 = User1Mapper.INSTANCE.to(userDTO);
            }
            System.out.println("Mapstruct 100W次转换耗时:" + (System.currentTimeMillis() - start));
            System.out.println();
        }
    }
}
