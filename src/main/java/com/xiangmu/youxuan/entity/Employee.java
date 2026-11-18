package com.xiangmu.youxuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Integer age;
    private Integer sex;
    private String idNumber;
    private Integer status;
    private Integer employeeType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
