package com.xiangmu.youxuan.vo.employeeVO;

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
public class EAftersaleVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;                    // 售后单主键ID
    private String afterSaleNo;         // 售后单号(雪花ID/时间戳生成，唯一)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;               // 关联的订单ID
    private Long orderItemId;           // 关联的订单明细ID(可空，整单退款时为null)
    private Long userId;                // 申请人(用户ID)
    private Integer type;               // 类型: 0退款 1换货 2投诉
    private String reason;              // 退款原因
    private BigDecimal amount;          // 退款金额
    private String description;         // 详细描述
    private String images;              // 凭证图片(逗号分隔URL)
    private Integer status;             // 状态: 0待处理 1已同意 2已拒绝 3退款中 4已完成 5已关闭
    private Long handlerId;             // 处理客服ID(员工表)
    private String handleNote;          // 客服处理意见
    private LocalDateTime handleTime;   // 客服处理时间
    private LocalDateTime completeTime; // 售后完成时间
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 修改时间
}
