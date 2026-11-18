package com.xiangmu.youxuan.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePageQueryDTO {
    Integer page;
    Integer pageSize;
    String name;
    Integer status;
}
