package com.xiangmu.youxuan.vo.employeeVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EEmployeeVO {
    private Long id;
    private String username;
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
