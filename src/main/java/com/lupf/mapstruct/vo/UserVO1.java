package com.lupf.mapstruct.vo;


import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class UserVO1 {
    private String name;

    private Integer age;

    // 类型和VO对象不同
    private String createTime;

    // 类型和VO对象不同
    private String updateTime;

    private String wallet;

    private String deposit;
}
