package com.xiangmu.youxuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer payMethod;
    private Integer status;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;//发货时间
    private LocalDateTime finishTime;//完成时间
    private LocalDateTime cancelTime;//取消时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
