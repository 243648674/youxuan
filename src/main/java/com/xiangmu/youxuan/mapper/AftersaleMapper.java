package com.xiangmu.youxuan.mapper;

import com.xiangmu.youxuan.dto.aftersale.AftersalePageQueryDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersaleQueryDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersaleUpdateDTO;
import com.xiangmu.youxuan.entity.Aftersale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AftersaleMapper {
    // 保存服务端已设置主键的售后单，将初始状态设为待处理
    void insert(Aftersale aftersale);
    // 按售后单ID和旧状态更新状态及处理时间，首次处理时记录处理人，完成时记录完成时间，返回影响行数
    int update(AftersaleUpdateDTO aftersaleUpdateDTO);
    // 将指定的待处理或已同意售后单改为已关闭
    void updateDelete(@Param("id") Long id);
    // 根据售后单ID从数据库查询售后信息
    Aftersale selectById(Long id);
    // 统计指定订单下状态不为5的售后单数量，包含已拒绝和已完成的记录
    Long selectByOrderIdAndStatus(@Param("orderId") Long orderId);
    // 按订单ID、用户ID、售后类型和处理员工ID等条件查询售后单列表
    List<Aftersale> selectByQuery(AftersaleQueryDTO aftersaleQueryDTO);
    // 按筛选条件统计数据库中的售后单条数
    Long count(AftersalePageQueryDTO aftersalePageQueryDTO);
    // 按筛选条件和分页范围从数据库查询本页售后单列表
    List<Aftersale> select(@Param("begin") Integer begin,@Param("dto") AftersalePageQueryDTO aftersalePageQueryDTO);
}
