package com.xiangmu.youxuan.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OrderDTO {
        // 前端购物车中的所有商品
        private List<OrderItemDTO> orderItemDTOS;
        private Integer payMethod;//支付方式
        private String receiverName;
        private String receiverPhone;
        private String receiverAddress;
    }
