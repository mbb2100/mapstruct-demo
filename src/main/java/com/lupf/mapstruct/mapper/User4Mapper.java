package com.lupf.mapstruct.mapper;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.vo.UserVO1;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface User4Mapper extends BaseMapper<UserDTO, UserVO1>{
    User4Mapper INSTANCE = Mappers.getMapper(User4Mapper.class);

    @Mappings({
            // 要忽略的字段
            @Mapping(target = "createTime",ignore = true)
    })
    @Override
    UserVO1 to(UserDTO var1);
}
