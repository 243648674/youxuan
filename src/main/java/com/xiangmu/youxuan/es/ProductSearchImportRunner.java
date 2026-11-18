package com.xiangmu.youxuan.es;

import com.xiangmu.youxuan.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "app.search",
        name = "import-enabled",
        havingValue = "true"
)
public class ProductSearchImportRunner implements ApplicationRunner {

    private final ProductSearchService productSearchService;

    @Override
    public void run(ApplicationArguments args) {
        long count = productSearchService.importProducts();

        log.info("商品搜索数据导入完成，共导入 {} 条商品", count);
    }
}