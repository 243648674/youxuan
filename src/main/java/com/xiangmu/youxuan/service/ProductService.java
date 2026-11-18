package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.dto.product.ProductDTO;
import com.xiangmu.youxuan.dto.product.ProductPageQueryDTO;
import com.xiangmu.youxuan.dto.product.ProductUpdateDTO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EProductVO;
import com.xiangmu.youxuan.vo.userVO.UProductVO;

import java.util.List;

public interface ProductService {
    // 员工端：员工新增商品，将商品初始状态设为上架
    void insert(ProductDTO productDTO);

    // 员工端：员工根据商品ID修改指定商品的信息
    void update(ProductUpdateDTO productUpdateDTO);

    // 员工端：员工根据商品ID修改指定商品的上架或下架状态
    void updateStatus(ProductDTO productDTO);

    // 员工端：员工按筛选条件分页查询商品，返回本页商品和总条数
    EPageQueryVO<EProductVO> employeeSelect(ProductPageQueryDTO productPageQueryDTO);

    // 用户端：用户按分类查询已上架商品列表
    List<UProductVO> userSelect(Long categoryId);

    // 用户端：用户按商品名称模糊查询已上架商品列表
    List<UProductVO> userNameSelect(String name);
}
