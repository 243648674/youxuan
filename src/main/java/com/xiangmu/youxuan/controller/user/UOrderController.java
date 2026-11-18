package com.xiangmu.youxuan.controller.user;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.order.OrderDTO;
import com.xiangmu.youxuan.dto.order.OrderPageQueryDTO;
import com.xiangmu.youxuan.service.OrderService;
import com.xiangmu.youxuan.vo.userVO.UOrderDetailVO;
import com.xiangmu.youxuan.vo.userVO.UOrderVO;
import com.xiangmu.youxuan.vo.userVO.UQueryListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@RequiredArgsConstructor
public class UOrderController {
    private final OrderService orderService;
    // 用户创建待支付订单，按数据库商品价格计算金额并保存商品明细
    @PostMapping("/insert")
    public Result insert(@RequestBody OrderDTO orderDTO){
        orderService.insert(orderDTO);
        return Result.success(null);
    }
    // 用户支付本人订单，同时扣减商品库存并增加销量
    @PutMapping("/pay")
    public Result pay(@RequestParam Long id) {
        orderService.pay(id);
        return Result.success(null);
    }
    // 用户取消本人的待支付订单，将订单状态改为已取消
    @PutMapping("/updateDelete")
    public Result updateDelete(@RequestParam Long id){
        orderService.updateDelete(id);
        return Result.success(null);
    }
    // 用户按筛选条件分页查询本人的订单，返回本页订单和总条数
    @GetMapping("/select")
    public Result<UQueryListVO<UOrderVO>> select(OrderPageQueryDTO orderPageQueryDTO){
        UQueryListVO<UOrderVO> uOrderVOUQueryListVO=
                orderService.userSelect(orderPageQueryDTO);
        return Result.success(uOrderVOUQueryListVO);
    }
    // 用户根据订单ID查询本人订单的完整信息和商品明细
    @GetMapping("/selectById")
    public Result<UOrderDetailVO> selectById(@RequestParam Long id){
        UOrderDetailVO orderDetailVO = orderService.userSelectById(id);
        return Result.success(orderDetailVO);
    }
}
