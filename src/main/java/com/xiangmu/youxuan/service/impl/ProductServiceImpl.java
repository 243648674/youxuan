package com.xiangmu.youxuan.service.impl;

import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.config.RedissonConfig;
import com.xiangmu.youxuan.dto.product.ProductDTO;
import com.xiangmu.youxuan.dto.product.ProductPageQueryDTO;
import com.xiangmu.youxuan.dto.product.ProductUpdateDTO;
import com.xiangmu.youxuan.entity.Product;
import com.xiangmu.youxuan.es.ProductSearchImportRunner;
import com.xiangmu.youxuan.mapper.ProductMapper;
import com.xiangmu.youxuan.service.ProductSearchService;
import com.xiangmu.youxuan.service.ProductService;
import com.xiangmu.youxuan.service.cache.ProductCache;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EProductVO;
import com.xiangmu.youxuan.vo.userVO.UProductVO;
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
public class ProductServiceImpl implements ProductService {
    private final ProductCache productCache;
    private final ProductMapper productMapper;
    private final RedissonConfig redissonConfig;
    private final ProductSearchService productSearchService;
    private String getKey(Long categoryId){
        return String.valueOf(categoryId);
    }
    //新增商品
    @Transactional
    @Override
    public void insert(ProductDTO productDTO){
        Product product = productMapper.selectByName(productDTO.getName());
        if (product != null){
            throw new BusinessException("商品已存在");
        }
        productMapper.insert(productDTO);
        productCache.deleteCategoryCache(getKey(productDTO.getCategoryId()));
    }
    //修改商品
    @Transactional
    @Override
    public void update(ProductUpdateDTO productUpdateDTO){
        Product product = productMapper.selectById(productUpdateDTO.getId());
        if (product == null){
            throw new BusinessException("商品不存在");
        }
        if (productUpdateDTO.getName() != null) {
            Product sameNameProduct = productMapper.selectByName(productUpdateDTO.getName());
            if (sameNameProduct != null
                    && !sameNameProduct.getId().equals(productUpdateDTO.getId())) {
                throw new BusinessException("商品已存在");
            }
        }
        productMapper.update(productUpdateDTO);
        if (productUpdateDTO.getCategoryId() == null){
            productCache.deleteCategoryCache(getKey(product.getCategoryId()));
        }else {
            productCache.deleteCategoryCache(getKey(product.getCategoryId()));
            productCache.deleteCategoryCache(getKey(productUpdateDTO.getCategoryId()));
        }
    }
    //修改商品状态
    @Transactional
    @Override
    public void updateStatus(ProductDTO productDTO){
        Product product = productMapper.selectById(productDTO.getId());
        if (product == null){
            throw new BusinessException("商品不存在");
        }
        productMapper.updateStatus(productDTO);
            productCache.deleteCategoryCache(getKey(product.getCategoryId()));
    }
    @Override
    public EPageQueryVO<EProductVO> employeeSelect(
            ProductPageQueryDTO productPageQueryDTO){
        Integer begin = (productPageQueryDTO.getPage()-1)*productPageQueryDTO.getPageSize();
        Long count = productMapper.count(productPageQueryDTO);
        List<Product> products = productMapper.select(begin,productPageQueryDTO);
        List<EProductVO> productVOS = new ArrayList<>();
        for (Product product : products) {
            EProductVO productVO = new EProductVO();
            BeanUtils.copyProperties(product,productVO);
            productVOS.add(productVO);
        }
        EPageQueryVO<EProductVO> ePageQueryVO = new EPageQueryVO<>();
        ePageQueryVO.setCount(count);
        ePageQueryVO.setPageQueryVO(productVOS);
        return ePageQueryVO;
    }

    @Override
    public List<UProductVO> userSelect(Long categoryId) {
        String key = getKey(categoryId);
        List<UProductVO> uProductVOS = productCache.getProductCache(key);
        if (uProductVOS != null) {
            return uProductVOS;
        }
        RLock lock = redissonConfig.redissonClient().getLock("lock:product:list" + categoryId);
        boolean b;
        try {
            b = lock.tryLock(5, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            throw new BusinessException("商品查询已中断");
        }
        if (b) {
            try {
                List<UProductVO> u = productCache.getProductCache(key);
                if (u != null) {
                    return u;
                }
                List<Product> products = productMapper.selectUserList(categoryId);
                List<UProductVO> userProductVOS =
                        new ArrayList<>();
                for (Product product : products) {
                    UProductVO userProductVO =
                            new UProductVO();
                    BeanUtils.copyProperties(product, userProductVO);
                    userProductVOS.add(userProductVO);
                }
                productCache.setProductCache(key, userProductVOS);
                return userProductVOS;
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException("数据正在加载，请稍后重试");
        }
    }

    @Override
    public List<UProductVO> userNameSelect(String name) {
        return productSearchService.searchByName(name);
    }
}
