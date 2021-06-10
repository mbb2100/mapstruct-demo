package com.lupf.mapstruct.mapper;

import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.vo.UserVO1;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author LENOVO
 * @title: User1Mapper
 * @projectName mapstruct-demo
 * @description: TODO
 * @date 2021/6/9 23:13
 */
@Mapper
public interface User1Mapper extends BaseMapper<UserDTO, UserVO1>{
    User1Mapper INSTANCE = Mappers.getMapper(User1Mapper.class);

//    @Mappings({
//            @Mapping(source = "createTime",target = "createTime",dateFormat = "yyyyMMdd")
//    })
//    @Override
//    UserVO1 to(UserDTO var1);
}
