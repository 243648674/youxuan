package com.xiangmu.youxuan.dto.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersalePageQueryDTO {
    private Integer page;               // 页码
    private Integer pageSize;           // 每页条数
    private Integer status;             // 状态: 0待处理 1已同意 2已拒绝 3退款中 4已完成 5已关闭
    private Integer type;               // 类型: 0退款 1换货 2投诉
    private Long orderId;               // 关联的订单ID
    private Long userId;                // 申请人(用户ID)
}
