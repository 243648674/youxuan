package com.xiangmu.youxuan.vo.employeeVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ECategoryVO {
    private Long id;
    private String name;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
