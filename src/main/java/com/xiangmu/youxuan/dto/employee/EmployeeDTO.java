package com.xiangmu.youxuan.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Integer age;
    private Integer sex;
    private String idNumber;
    private Integer status;

}
