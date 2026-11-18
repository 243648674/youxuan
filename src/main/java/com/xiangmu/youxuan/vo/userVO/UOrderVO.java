package com.xiangmu.youxuan.vo.userVO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UOrderVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private BigDecimal payAmount;
    private Integer payMethod;
    private Integer status;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime finishTime;
    private LocalDateTime cancelTime;
    private LocalDateTime createTime;
}
