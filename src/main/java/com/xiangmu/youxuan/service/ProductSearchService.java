package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.es.ProductDocument;
import com.xiangmu.youxuan.entity.Product;
import com.xiangmu.youxuan.mapper.ProductMapper;
import com.xiangmu.youxuan.vo.userVO.UProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductMapper productMapper;

    // 创建索引，并分批导入现有商品
    public long importProducts() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(ProductDocument.class);
        if (!indexOperations.exists()) {
            boolean created = indexOperations.createWithMapping();
            if (!created) {
                throw new RuntimeException("商品搜索索引创建失败");
            }
        }
        Long lastId = null;
        int batchSize = 200;
        long total = 0;
        while (true) {
            List<Product> products = productMapper.selectSearchBatch(lastId, batchSize);
            if (products.isEmpty()) {
                break;
            }
            List<ProductDocument> documents = new ArrayList<>();
            for (Product product : products) {
                ProductDocument document = new ProductDocument();
                BeanUtils.copyProperties(product, document);
                documents.add(document);
            }
            elasticsearchOperations.save(documents);
            total = total + products.size();
            Product lastProduct = products.get(products.size() - 1);
            lastId = lastProduct.getId();
        }
        indexOperations.refresh();
        return total;
    }
    // 按商品名称搜索已上架商品
    public List<UProductVO> searchByName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("搜索关键词不能为空");
        }
        //创建查询条件
        // 名称经过分词后，要求所有搜索词都匹配
        Criteria criteria = new Criteria("name");
        criteria.matchesAll(name);
        criteria = criteria.and("status").is(1);
        CriteriaQuery query = new CriteriaQuery(criteria);
        query.setPageable(PageRequest.of(0, 20));

        SearchHits<ProductDocument> searchHits =
                elasticsearchOperations.search(
                        query,
                        ProductDocument.class
                );

        List<UProductVO> result = new ArrayList<>();

        for (SearchHit<ProductDocument> searchHit : searchHits) {
            ProductDocument document = searchHit.getContent();

            UProductVO productVO = new UProductVO();
            BeanUtils.copyProperties(document, productVO);

            result.add(productVO);
        }

        return result;
    }
}

