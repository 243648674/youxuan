package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.dto.user.UserDTO;
import com.xiangmu.youxuan.dto.user.UserLoginDTO;
import com.xiangmu.youxuan.dto.user.UserPageQueryDTO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EUserVO;
import com.xiangmu.youxuan.vo.userVO.UUserLoginVO;
import com.xiangmu.youxuan.vo.userVO.UUserVO;

public interface UserService {
    // 用户端：校验用户登录信息和账号状态，返回用户信息及登录令牌
    UUserLoginVO userLogin(UserLoginDTO userLoginDTO);

    // 用户端：注册用户账号，将编码后的密码保存到数据库
    void insert(UserDTO userDTO);

    // 用户端：用户查询当前登录账号的个人信息
    UUserVO userSelectMy();

    // 员工端：员工按筛选条件分页查询用户，返回本页用户和总条数
    EPageQueryVO<EUserVO> employeeSelect(UserPageQueryDTO userPageQueryDTO);

    // 用户端：用户停用当前登录账号，将账号状态由1改为0
    void updateStatus();
}
