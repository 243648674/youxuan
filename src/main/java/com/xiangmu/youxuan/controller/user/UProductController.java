package com.xiangmu.youxuan.controller.user;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.service.ProductService;
import com.xiangmu.youxuan.vo.userVO.UProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/product")
@RequiredArgsConstructor
public class UProductController {
    private final ProductService productService;

    // 用户按分类查询已上架商品列表
    @GetMapping("/select")
    public Result<List<UProductVO>> select(@RequestParam("categoryId") Long categoryId) {
        List<UProductVO> productVOS = productService.userSelect(categoryId);
        return Result.success(productVOS);
    }

    // 用户按商品名称模糊查询已上架商品列表
    @GetMapping("/nameSelect")
    public Result<List<UProductVO>> nameSelect(@RequestParam("name") String name) {
        List<UProductVO> productVOS = productService.userNameSelect(name);
        return Result.success(productVOS);
    }

}
