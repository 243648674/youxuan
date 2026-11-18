package com.xiangmu.youxuan.mapper;
import com.xiangmu.youxuan.dto.order.OrderPageQueryDTO;
import com.xiangmu.youxuan.dto.order.OrderUpdateDTO;
import com.xiangmu.youxuan.entity.Order;
import com.xiangmu.youxuan.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface OrderMapper {
    // 根据订单ID从数据库查询订单信息
    Order selectById(Long id);
    // 按订单ID和当前用户ID查询本人订单信息
    Order selectUserById(@Param("id") Long id, @Param("userId") Long userId);
    // 根据订单ID从数据库查询该订单的商品明细列表
    List<OrderItem> selectItemByOrderId(Long id);
    // 保存服务端已设置主键的待支付订单
    void insert(Order order);
    // 保存一条订单商品明细，并回填数据库生成的明细ID
    void insertItem(OrderItem orderItem);
    // 按订单ID和旧状态更新订单状态及对应时间，返回影响行数
    int updateStatus(OrderUpdateDTO orderUpdateDTO);
    // 将指定的待支付订单改为已取消，并记录取消时间，返回影响行数
    int updateDelete(Long id);
    // 将指定的已支付、已发货或已完成订单标记为已退款，返回影响行数
    int refund(@Param("id") Long id);
    // 将指定用户的待支付订单改为已支付，并记录支付时间，返回影响行数
    Integer pay(@Param("id") Long id, @Param("userId") Long userId);
    // 按筛选条件统计数据库中的订单条数
    Long count(OrderPageQueryDTO orderPageQueryDTO);
    // 按筛选条件和分页范围从数据库查询本页订单列表
    List<Order> select(@Param("begin") Integer begin, @Param("orderPageQueryDTO") OrderPageQueryDTO orderPageQueryDTO);
    // 按指定用户ID查询该用户的完整订单列表，不分页
    List<Order> selectUserList(@Param("orderPageQueryDTO") OrderPageQueryDTO orderPageQueryDTO);
}
