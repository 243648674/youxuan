package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.dto.category.CategoryDTO;
import com.xiangmu.youxuan.vo.employeeVO.ECategoryVO;
import com.xiangmu.youxuan.vo.userVO.UCategoryVO;

import java.util.List;

public interface CategoryService {
    // 员工端：员工新增分类
    void insert(CategoryDTO categoryDTO);

    // 员工端：员工修改指定分类
    void update(CategoryDTO categoryDTO);

    // 员工端：员工修改指定分类的启用状态
    void updateStatus(CategoryDTO categoryDTO);

    // 员工端：删除已停用且没有商品的分类
    void delete(Long id);

    // 员工端：查询全部分类，返回分类的全部字段，包含启用和停用分类
    List<ECategoryVO> employeeSelect();

    // 用户端：查询全部启用分类，只返回分类ID和名称
    List<UCategoryVO> userSelect();
}
