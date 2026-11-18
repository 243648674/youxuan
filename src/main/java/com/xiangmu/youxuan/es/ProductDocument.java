package com.xiangmu.youxuan.es;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
@Data
@Document(indexName = "youxuan_product", createIndex = false)
public class ProductDocument {
    private Long id;
    private Long categoryId;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;
    @Field(type = FieldType.Scaled_Float, scalingFactor = 100)
    private BigDecimal price;
    @Field(type = FieldType.Integer)
    private Integer status;
    @Field(type = FieldType.Integer)
    private Integer sales;
    private String image;

}