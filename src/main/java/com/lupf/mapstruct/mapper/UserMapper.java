package com.lupf.mapstruct.mapper;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserDTO, UserVO> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
