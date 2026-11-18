package com.xiangmu.youxuan.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long ItemId;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;      // 下单时单价快照
    private Integer quantity;
    private BigDecimal payPrice; // = price * quantity
}
