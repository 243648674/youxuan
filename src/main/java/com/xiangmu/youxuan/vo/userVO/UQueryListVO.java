package com.xiangmu.youxuan.vo.userVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UQueryListVO<T> {
    Long count;
    List<T> list;
}
