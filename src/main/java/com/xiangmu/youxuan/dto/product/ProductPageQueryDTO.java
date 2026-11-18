package com.xiangmu.youxuan.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageQueryDTO {
    private Integer page;      //页码
    private Integer pageSize;  //每页条数
    private String name;       //商品名（模糊）
    private Long categoryId;   //分类ID（精确，表上有 idx_category 索引）
    private Integer status;    //状态：1上架 0下架
}
