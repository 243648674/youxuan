package com.xiangmu.youxuan.vo.userVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UOrderDetailVO {
    private UOrderVO order;
    private List<UOrderItemVO> items;
}
