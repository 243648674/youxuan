package com.xiangmu.youxuan.mapper;

import com.xiangmu.youxuan.dto.product.ProductDTO;
import com.xiangmu.youxuan.dto.product.ProductPageQueryDTO;
import com.xiangmu.youxuan.dto.product.ProductUpdateDTO;
import com.xiangmu.youxuan.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // 向数据库新增商品，将商品初始状态设为上架
    void insert(ProductDTO productDTO);
    // 根据商品ID更新传入的分类、名称、描述、价格、库存和图片
    void update(ProductUpdateDTO productUpdateDTO);
    // 修改库存，负数表示扣减，返回影响行数
    int updateStock(@Param("id") Long id,@Param("stock") Integer stock);
    // 修改销量，负数表示扣减，返回影响行数
    int updateSales(@Param("id") Long id,@Param("quantity") Integer quantity);
    // 根据商品ID修改商品的上架或下架状态
    void updateStatus(ProductDTO productDTO);
    // 根据商品ID从数据库查询商品信息及分类名称
    Product selectById(Long id);
    // 根据商品名称从数据库查询商品信息及分类名称
    Product selectByName(String name);
    // 根据分类ID统计全部商品数量，包含上架和下架商品
    Long countByCategoryId(Long categoryId);
    // 按筛选条件统计数据库中的商品条数
    Long count(ProductPageQueryDTO productPageQueryDTO);
    // 按分类ID查询全部已上架商品
    List<Product> selectUserList(@Param("categoryId") Long categoryId);
    // 按商品名称模糊查询全部匹配的已上架商品
    List<Product> nameSelect(@Param("name") String name);
    // 按筛选条件和分页范围从数据库查询本页商品列表及分类名称
    List<Product> select(@Param("begin") Integer begin,@Param("productPageQueryDTO") ProductPageQueryDTO productPageQueryDTO);



    // 按商品ID递增，分批读取需要导入ES的商品
    List<Product> selectSearchBatch(
            @Param("lastId") Long lastId,
            @Param("batchSize") Integer batchSize);
}
