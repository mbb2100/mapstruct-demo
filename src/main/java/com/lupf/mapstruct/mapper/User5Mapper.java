package com.lupf.mapstruct.mapper;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.vo.UserVO1;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 自定义日期格式转换映射器
 * uses = DateMapper.class
 */
@Mapper(uses = DateMapper.class)
public interface User5Mapper extends BaseMapper<UserDTO, UserVO1> {
    User5Mapper INSTANCE = Mappers.getMapper(User5Mapper.class);
}