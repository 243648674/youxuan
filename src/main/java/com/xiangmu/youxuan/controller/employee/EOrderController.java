package com.xiangmu.youxuan.controller.employee;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.order.OrderPageQueryDTO;
import com.xiangmu.youxuan.dto.order.OrderUpdateDTO;
import com.xiangmu.youxuan.service.OrderService;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EOrderDetailVO;
import com.xiangmu.youxuan.vo.employeeVO.EOrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/order")
@RequiredArgsConstructor
public class EOrderController {
    private final OrderService orderService;

    // 员工将已支付订单推进为已发货，或将已发货订单推进为已完成
    @PutMapping("/update")
    public Result update(@RequestBody OrderUpdateDTO orderUpdateDTO) {
        orderService.update(orderUpdateDTO);
        return Result.success(null);
    }

    // 员工按筛选条件分页查询订单，返回本页订单和总条数
    @GetMapping("/select")
    public Result<EPageQueryVO<EOrderVO>> select(OrderPageQueryDTO orderPageQueryDTO) {
        EPageQueryVO<EOrderVO> ePageQueryVO =
                orderService.employeeSelect(orderPageQueryDTO);
        return Result.success(ePageQueryVO);
    }

    // 员工根据订单ID查询订单的完整信息和商品明细
    @GetMapping("/selectById")
    public Result<EOrderDetailVO> selectById(@RequestParam Long id) {
        EOrderDetailVO detailVO = orderService.employeeSelectById(id);
        return Result.success(detailVO);
    }
}
