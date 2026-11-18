package com.xiangmu.youxuan.vo.employeeVO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EOrderVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long userId;
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
