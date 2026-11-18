package com.xiangmu.youxuan.vo.employeeVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EProductVO {
    private Long id;                  //主键
    private Long categoryId;          //分类ID
    private String categoryName;      //分类名字
    private String name;              //商品名
    private String description;       //商品描述
    private BigDecimal price;         //价格
    private Integer stock;            //库存
    private Integer sales;            //销量
    private String image;             //主图URL
    private Integer status;           //状态：1上架 0下架
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
