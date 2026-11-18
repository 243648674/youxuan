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
public class UUserVoucherVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;                  // 用户领券记录ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long voucherId;           // 优惠券规则ID
    private String title;
    private String subTitle;
    private String rules;
    private Integer type;
    private BigDecimal minAmount;
    private BigDecimal discountAmount;
    private BigDecimal discountRate;
    private Integer status;           // 0未使用，1已使用，2已占用
    private LocalDateTime receiveTime;
    private LocalDateTime useBeginTime;
    private LocalDateTime useEndTime;
}
