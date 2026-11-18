package com.xiangmu.youxuan.service.impl;

import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.config.RedissonConfig;
import com.xiangmu.youxuan.dto.category.CategoryDTO;
import com.xiangmu.youxuan.entity.Category;
import com.xiangmu.youxuan.mapper.CategoryMapper;
import com.xiangmu.youxuan.mapper.ProductMapper;
import com.xiangmu.youxuan.service.CategoryService;
import com.xiangmu.youxuan.service.cache.CategoryCache;
import com.xiangmu.youxuan.vo.employeeVO.ECategoryVO;
import com.xiangmu.youxuan.vo.userVO.UCategoryVO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryCache categoryCache;
    private final ProductMapper productMapper;
    private final RedissonConfig redissonConfig;
    //新增分类
    @Transactional
    @Override
    public void insert(CategoryDTO categoryDTO){
        Category category = categoryMapper.selectByName(categoryDTO.getName());
        if (category != null){
            throw new BusinessException("分类已存在");
        }
        categoryMapper.insert(categoryDTO);
        categoryCache.deleteCategoryCache();
    }

    //修改分类
    @Transactional
    @Override
    public void update(CategoryDTO categoryDTO){
        Category category = categoryMapper.selectById(categoryDTO.getId());
        if (category == null){
            throw new BusinessException("分类不存在");
        }
        if (categoryDTO.getName() != null) {
            Category sameNameCategory = categoryMapper.selectByName(categoryDTO.getName());
            if (sameNameCategory != null
                    && !sameNameCategory.getId().equals(categoryDTO.getId())) {
                throw new BusinessException("分类已存在");
            }
        }
        categoryMapper.update(categoryDTO);
        categoryCache.deleteCategoryCache();
    }
    //修改分类状态
    @Transactional
    @Override
    public void updateStatus(CategoryDTO categoryDTO){
        Category category = categoryMapper.selectById(categoryDTO.getId());
        if (category == null){
            throw new BusinessException("分类不存在");
        }
        categoryMapper.updateStatus(categoryDTO);
        categoryCache.deleteCategoryCache();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Long count = productMapper.countByCategoryId(id);
        if (count > 0) {
            throw new BusinessException("分类下仍有商品，不能删除");
        }

        int rows = categoryMapper.delete(id);
        if (rows != 1) {
            throw new BusinessException("分类不存在或未停用，不能删除");
        }
        categoryCache.deleteCategoryCache();
    }

    @Override
    public List<ECategoryVO> employeeSelect(){
        List<Category> categories = categoryMapper.select(null);
        List<ECategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories) {
            ECategoryVO categoryVO = new ECategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            categoryVOS.add(categoryVO);
        }
        return categoryVOS;
    }

    @Override
    public List<UCategoryVO> userSelect(){
        List<UCategoryVO> uCategoryVOS = categoryCache.getCategoryCache();
        if (uCategoryVOS != null){
            return uCategoryVOS;
        }
        RLock lock = redissonConfig.redissonClient().getLock("lock:category:list");
        boolean b;
        try {
             b = lock.tryLock(5, TimeUnit.SECONDS);
        } catch (InterruptedException e){
            throw new BusinessException("分类查询已中断");
        }
        if (b) {
            try {
                List<UCategoryVO> u = categoryCache.getCategoryCache();
                if (u != null){
                    return u;
                }
                List<Category> categories = categoryMapper.select(1);
                List<UCategoryVO> userCategoryVOS =
                        new ArrayList<>();
                for (Category category : categories) {
                    UCategoryVO userCategoryVO =
                            new UCategoryVO();
                    BeanUtils.copyProperties(category,userCategoryVO);
                    userCategoryVOS.add(userCategoryVO);
                }

                categoryCache.setCategoryCache(userCategoryVOS);
                return userCategoryVOS;
            } finally {
                lock.unlock();
            }
        }else {
            throw new BusinessException("数据正在加载，请稍后重试");
        }
    }
}
