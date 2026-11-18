package com.xiangmu.youxuan.mq;

import lombok.Data;

@Data
public class VoucherReceiveMessage {
    private Long userId;
    private Long voucherId;
    private String  lockValue;
}
