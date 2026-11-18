package com.xiangmu.youxuan.controller.user;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.service.CategoryService;
import com.xiangmu.youxuan.vo.userVO.UCategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/category")
@RequiredArgsConstructor
public class UCategoryController {
    private final CategoryService categoryService;

    // 用户查询全部启用分类，只返回ID和名称
    @GetMapping("/select")
    public Result<List<UCategoryVO>> select() {
        List<UCategoryVO> categories = categoryService.userSelect();
        return Result.success(categories);
    }
}
