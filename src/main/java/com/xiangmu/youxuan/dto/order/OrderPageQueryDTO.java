package com.xiangmu.youxuan.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageQueryDTO {
    Integer page;
    Integer pageSize;
    Integer status;
    Integer payMethod;
    Long userId;
}
