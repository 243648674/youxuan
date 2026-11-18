package com.xiangmu.youxuan.vo.employeeVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EEmployeeLoginVO {
    private Long id;
    private String username;
    private String name;
    private Integer employeeType;
    private String token;
}
