package com.lupf.mapstruct.mapper;


import com.lupf.mapstruct.dto.AddressDTO;
import com.lupf.mapstruct.dto.UserDTO;
import com.lupf.mapstruct.vo.UserVO2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", disableSubMappingMethodsGeneration = true)
public interface User2Mapper {
    User2Mapper INSTANCE = Mappers.getMapper(User2Mapper.class);

    // 如果无特殊字段，可以不配置Mappings
    // 会自动把两个源对象中的属性复制到咪表对象
    @Mappings({
            @Mapping(source = "userDTO.name", target = "name"),
            @Mapping(source = "addressDTO.country", target = "country")
    })
    UserVO2 to(UserDTO userDTO, AddressDTO addressDTO);
}
