package com.xiangmu.youxuan.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherQueryDTO {
    private Integer status; // 0未使用，1已使用，2已占用
    private Integer type;   // 0满减券，1打折券
}
