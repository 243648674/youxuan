package com.xiangmu.youxuan.mapper;

import com.xiangmu.youxuan.dto.category.CategoryDTO;
import com.xiangmu.youxuan.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 向数据库新增分类，并回填分类主键ID
    void insert(CategoryDTO categoryDTO);
    // 根据分类ID更新传入的分类名称和启用状态
    void update(CategoryDTO categoryDTO);
    // 根据分类ID修改分类的启用或停用状态
    void updateStatus(CategoryDTO categoryDTO);
    // 根据分类ID删除停用分类，返回删除行数
    int delete(Long id);
    // 查询全部分类；status为null时查询全部状态，否则只查询指定状态
    List<Category> select(@Param("status") Integer status);
    // 根据分类ID从数据库查询分类信息
    Category selectById(Long id);
    // 根据分类名称从数据库查询分类信息
    Category selectByName(String name);

}
