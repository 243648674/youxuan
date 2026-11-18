package com.xiangmu.youxuan.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginDTO {
    private String username;
    private String password;
}
