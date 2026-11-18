package com.xiangmu.youxuan.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;              //主键（修改时必填）
    private Long categoryId;      //分类ID
    private String name;          //商品名
    private String description;   //商品描述
    private BigDecimal price;     //价格
    private Integer stock;        //库存
    private Integer sales;        //销量
    private String image;         //主图URL
    private Integer status;       //状态：1上架 0下架
}
