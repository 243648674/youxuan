package com.xiangmu.youxuan.controller.employee;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.category.CategoryDTO;
import com.xiangmu.youxuan.service.CategoryService;
import com.xiangmu.youxuan.vo.employeeVO.ECategoryVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/category")
public class ECategoryController {
    private final CategoryService categoryService;
    // 注入分类业务组件，供当前Controller的方法调用
    public ECategoryController(CategoryService categoryService){
        this.categoryService=categoryService;
    }
    // 员工新增分类
    @PostMapping("/insert")
    public Result insert(@RequestBody CategoryDTO categoryDTO){
        categoryService.insert(categoryDTO);
        return Result.success(null);
    }
    // 员工修改指定分类
    @PutMapping("/update")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success(null);
    }
    // 员工修改指定分类的启用状态
    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestBody CategoryDTO categoryDTO){
        categoryService.updateStatus(categoryDTO);
        return Result.success(null);
    }
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Long id){
        categoryService.delete(id);
        return Result.success(null);
    }
    // 员工查询全部分类，包含全部字段及启用、停用分类
    @GetMapping("/select")
    public Result<List<ECategoryVO>> select(){
        List<ECategoryVO> categories = categoryService.employeeSelect();
        return Result.success(categories);
    }
}
