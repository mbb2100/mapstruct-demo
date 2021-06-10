package com.lupf.mapstruct.mapper;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.vo.UserVO2;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 嵌套对象的映射
 */
@Mapper
public interface User9Mapper extends BaseMapper<UserDTO, UserVO2> {
    User9Mapper INSTANCE = Mappers.getMapper(User9Mapper.class);

    @Mapping(source = "addressDTO.country", target = "country")
    @Override
    UserVO2 to(UserDTO userDTO);

    // 反向配置
    @InheritInverseConfiguration(name = "to")
    @Override
    UserDTO from(UserVO2 var1);
}