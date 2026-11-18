package com.xiangmu.youxuan.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {
    private Long id;              //商品ID
    private Long categoryId;      //分类ID
    private String name;          //商品名
    private String description;   //商品描述
    private BigDecimal price;     //价格
    private Integer stock;        //库存
    private String image;         //主图URL
}
