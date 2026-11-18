package com.xiangmu.youxuan.dto.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleDTO {
    private Long orderId;               // 关联的订单ID
    private Long orderItemId;           // 关联的订单明细ID(可空，整单退款时为null)
    private Long userId;                // 申请人(用户ID)
    private Integer type;               // 类型: 0退款 1换货 2投诉
    private String reason;              // 退款原因
    private BigDecimal amount;          // 退款金额
    private String description;         // 详细描述
    private String images;              // 凭证图片(逗号分隔URL)
}
