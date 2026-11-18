package com.xiangmu.youxuan.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDTO {
    private Long id;
    private Integer status;
    //修改点：新增，由 Service 回填旧状态，客户端不用传
    private Integer currentStatus;
}
