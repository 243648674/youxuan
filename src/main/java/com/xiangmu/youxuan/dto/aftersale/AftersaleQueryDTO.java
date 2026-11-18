package com.xiangmu.youxuan.dto.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleQueryDTO {
    private Long orderId;               // 关联的订单ID
    private Long userId;                // 申请人(用户ID)
    private Integer type;               // 类型: 0退款 1换货 2投诉
    private Long handlerId;             // 处理客服ID(员工表)
}
