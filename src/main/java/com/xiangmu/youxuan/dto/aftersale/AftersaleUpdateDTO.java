package com.xiangmu.youxuan.dto.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleUpdateDTO {
    private Long id;                    // 售后单ID
    private Integer status;             // 1同意 2拒绝 4完成
    private Integer currentStatus;      // 当前状态，Service回填，客户端不用传
    private Long handlerId;             // 处理客服ID(员工表)，第一次处理时必填，后续推进须与首次一致
    private String handleNote;          // 客服处理意见
}
