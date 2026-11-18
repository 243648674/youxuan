package com.xiangmu.youxuan.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherPageQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer status; // 1启用，0停用
    private Integer type;   // 0满减券，1打折券
}
