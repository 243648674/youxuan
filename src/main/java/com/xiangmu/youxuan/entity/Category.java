package com.xiangmu.youxuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
