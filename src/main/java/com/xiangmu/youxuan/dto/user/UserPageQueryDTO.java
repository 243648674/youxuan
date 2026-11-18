package com.xiangmu.youxuan.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Integer status;
}
