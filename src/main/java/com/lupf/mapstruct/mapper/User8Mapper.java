package com.lupf.mapstruct.mapper;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.vo.UserVO1;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * BigDecimal转换
 */
@Mapper
public interface User8Mapper extends BaseMapper<UserDTO, UserVO1> {
    User8Mapper INSTANCE = Mappers.getMapper(User8Mapper.class);

    @Mapping(source = "deposit", target = "deposit", numberFormat = "#.##E0")
    @Override
    UserVO1 to(UserDTO var1);

    @Mapping(source = "deposit", target = "deposit", numberFormat = "#.##E0")
    @Override
    UserDTO from(UserVO1 var1);
}