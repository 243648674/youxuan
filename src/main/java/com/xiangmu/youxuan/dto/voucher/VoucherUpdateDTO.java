package com.xiangmu.youxuan.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherUpdateDTO {
    private Long id;
    private String title;             // 优惠券名称
    private String subTitle;          // 优惠说明，例如满100减20、八折优惠
    private String rules;             // 使用规则
    private BigDecimal minAmount;     // 使用门槛，单位元
    private BigDecimal discountAmount; // 满减券减免金额，单位元
    private BigDecimal discountRate;  // 打折券折扣比例，例如0.80表示八折
    private Integer stock;            // 可领取库存
    private LocalDateTime receiveBeginTime;
    private LocalDateTime receiveEndTime;
    private LocalDateTime useBeginTime;
    private LocalDateTime useEndTime;
}
