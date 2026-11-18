package com.xiangmu.youxuan.vo.userVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UUserLoginVO {
    private Long id;
    private String name;
    private String token;
}
