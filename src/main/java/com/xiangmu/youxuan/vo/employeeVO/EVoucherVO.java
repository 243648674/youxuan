package com.xiangmu.youxuan.vo.employeeVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EVoucherVO {
    private Long id;
    private String title;
    private String subTitle;
    private String rules;
    private Integer type;
    private BigDecimal minAmount;
    private BigDecimal discountAmount;
    private BigDecimal discountRate;
    private Integer stock;
    private LocalDateTime receiveBeginTime;
    private LocalDateTime receiveEndTime;
    private LocalDateTime useBeginTime;
    private LocalDateTime useEndTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
