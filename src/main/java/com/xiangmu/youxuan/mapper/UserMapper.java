package com.xiangmu.youxuan.mapper;

import com.xiangmu.youxuan.dto.user.UserPageQueryDTO;
import com.xiangmu.youxuan.dto.user.UserDTO;
import com.xiangmu.youxuan.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    // 根据登录用户名从数据库查询用户账号，包含密码编码值
    User userSelectByUsername(String username);
    // 向数据库新增用户账号，将账号初始状态设为启用
    void insert(UserDTO userDTO);
    // 按筛选条件统计数据库中的用户条数
    Long count(UserPageQueryDTO userPageQueryDTO);
    // 按筛选条件和分页范围从数据库查询本页用户列表
    List<User> select(@Param("begin") Integer begin,@Param("userPageQueryDTO") UserPageQueryDTO userPageQueryDTO);
    // 根据用户ID从数据库查询用户资料
    User selectById(Long id);
    // 将指定的启用用户账号设为停用，返回影响行数
    int updateStatus(Long id);
}
