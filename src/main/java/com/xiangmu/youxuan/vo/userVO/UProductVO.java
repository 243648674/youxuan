package com.xiangmu.youxuan.vo.userVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UProductVO {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer sales;
    private String image;
}
