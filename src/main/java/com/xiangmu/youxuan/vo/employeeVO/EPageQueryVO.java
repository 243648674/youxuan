package com.xiangmu.youxuan.vo.employeeVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EPageQueryVO<T> {
    private Long count;
    private List<T> pageQueryVO;
}
