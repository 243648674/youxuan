package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.dto.order.OrderDTO;
import com.xiangmu.youxuan.dto.order.OrderPageQueryDTO;
import com.xiangmu.youxuan.dto.order.OrderUpdateDTO;
import com.xiangmu.youxuan.vo.employeeVO.EOrderDetailVO;
import com.xiangmu.youxuan.vo.employeeVO.EOrderVO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.userVO.UOrderDetailVO;
import com.xiangmu.youxuan.vo.userVO.UOrderVO;
import com.xiangmu.youxuan.vo.userVO.UQueryListVO;

public interface OrderService {
    // 用户端：用户创建待支付订单，按数据库商品价格计算金额并保存商品明细
    void insert(OrderDTO orderDTO);

    // 用户端：支付本人订单，同时扣减商品库存并增加销量
    void pay(Long orderId);

    // 员工端：员工将已支付订单推进为已发货，或将已发货订单推进为已完成
    void update(OrderUpdateDTO orderUpdateDTO);

    // 用户端：用户取消本人的待支付订单，将订单状态改为已取消
    void updateDelete(Long id);

    // 员工端：员工按筛选条件分页查询订单，返回本页订单和总条数
    EPageQueryVO<EOrderVO> employeeSelect(OrderPageQueryDTO orderPageQueryDTO);

    // 用户端：用户按筛选条件分页查询本人的订单，返回订单和总条数
    UQueryListVO<UOrderVO> userSelect(
            OrderPageQueryDTO orderPageQueryDTO);

    // 员工端：根据订单ID查询订单的完整信息和商品明细
    EOrderDetailVO employeeSelectById(Long id);

    // 用户端：根据订单ID查询本人订单的完整信息和商品明细
    UOrderDetailVO userSelectById(Long id);

}
