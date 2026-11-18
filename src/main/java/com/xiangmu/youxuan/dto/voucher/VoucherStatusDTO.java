package com.xiangmu.youxuan.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherStatusDTO {
    private Long id;
    private Integer status; // 1启用，0停用
}
