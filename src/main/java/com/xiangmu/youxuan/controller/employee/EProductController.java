package com.xiangmu.youxuan.controller.employee;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.product.ProductDTO;
import com.xiangmu.youxuan.dto.product.ProductPageQueryDTO;
import com.xiangmu.youxuan.dto.product.ProductUpdateDTO;
import com.xiangmu.youxuan.service.ProductService;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EProductVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/product")
public class EProductController {
    private final ProductService productService;
    // 注入商品业务组件，供当前Controller的方法调用
    public EProductController(ProductService productService){
        this.productService=productService;
    }
    // 员工新增商品，将商品初始状态设为上架
    @PostMapping("/insert")
    public Result insert(@RequestBody ProductDTO productDTO){
        productService.insert(productDTO);
        return Result.success(null);
    }
    // 员工根据商品ID修改指定商品的信息
    @PutMapping("/update")
    public Result update(@RequestBody ProductUpdateDTO productUpdateDTO){
        productService.update(productUpdateDTO);
        return Result.success(null);
    }
    // 员工根据商品ID修改指定商品的上架或下架状态
    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestBody ProductDTO productDTO){
        productService.updateStatus(productDTO);
        return Result.success(null);
    }
    // 员工按筛选条件分页查询商品，返回本页商品和总条数
    @GetMapping("/select")
    public Result<EPageQueryVO<EProductVO>> select(
            ProductPageQueryDTO productPageQueryDTO){
        EPageQueryVO<EProductVO> ePageQueryVO =
                productService.employeeSelect(productPageQueryDTO);
        return Result.success(ePageQueryVO);
    }

}
