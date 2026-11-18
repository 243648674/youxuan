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
public class UAftersaleVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String afterSaleNo;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    private Long orderItemId;
    private Long userId;
    private Integer type;
    private String reason;
    private BigDecimal amount;
    private String description;
    private String images;
    private Integer status;
    private String handleNote;
    private LocalDateTime handleTime;
    private LocalDateTime completeTime;
    private LocalDateTime createTime;

}
