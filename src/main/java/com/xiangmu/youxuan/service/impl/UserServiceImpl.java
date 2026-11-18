package com.xiangmu.youxuan.service.impl;

import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.config.PasswordConfig;
import com.xiangmu.youxuan.context.BaseContext;
import com.xiangmu.youxuan.dto.user.UserDTO;
import com.xiangmu.youxuan.dto.user.UserLoginDTO;
import com.xiangmu.youxuan.dto.user.UserPageQueryDTO;
import com.xiangmu.youxuan.entity.User;
import com.xiangmu.youxuan.mapper.UserMapper;
import com.xiangmu.youxuan.service.UserService;
import com.xiangmu.youxuan.utils.JwtUtil;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EUserVO;
import com.xiangmu.youxuan.vo.userVO.UUserLoginVO;
import com.xiangmu.youxuan.vo.userVO.UUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    public final PasswordConfig passwordConfig;

    //用户登录
    @Override
    public UUserLoginVO userLogin(UserLoginDTO userLoginDTO){
        User user = userMapper.userSelectByUsername(userLoginDTO.getUsername());
        if (user == null){
            throw new BusinessException("用户不存在");
        }
        if (!passwordConfig.passwordEncoder().matches(userLoginDTO.getPassword(),user.getPassword())){
            throw new BusinessException("密码错误");
        }
        if (user.getStatus() == 0){
            throw new BusinessException("用户已被封禁");
        }
        UUserLoginVO userLoginVO = new UUserLoginVO();
        BeanUtils.copyProperties(user,userLoginVO);
        String token = JwtUtil.createUserJWT(user.getId());
        userLoginVO.setToken(token);
        return userLoginVO;
    }
    //用户注册
    @Transactional
    @Override
    public void insert(UserDTO userDTO){
        if (userMapper.userSelectByUsername(userDTO.getUsername()) != null){
            throw new BusinessException("用户已存在");
        }
        userDTO.setPassword(passwordConfig.passwordEncoder().encode(userDTO.getPassword()));
        userMapper.insert(userDTO);
    }
    //用户查询自己
    @Override
    public UUserVO userSelectMy() {
        User user = userMapper.selectById(BaseContext.getCurrentId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UUserVO userVO =
                new UUserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }
    //员工分页查询用户
    @Override
    public EPageQueryVO<EUserVO> employeeSelect(UserPageQueryDTO userPageQueryDTO){
        Integer begin = (userPageQueryDTO.getPage()-1)*userPageQueryDTO.getPageSize();
        List<User> users = userMapper.select(begin,userPageQueryDTO);
        List<EUserVO> userVOS = new ArrayList<>();
        for (User user : users) {
            EUserVO userVO = new EUserVO();
            BeanUtils.copyProperties(user,userVO);
            userVOS.add(userVO);
        }
        Long count = userMapper.count(userPageQueryDTO);
        EPageQueryVO<EUserVO> ePageQueryVO = new EPageQueryVO<>();
        ePageQueryVO.setPageQueryVO(userVOS);
        ePageQueryVO.setCount(count);
        return ePageQueryVO;
    }
    //删除用户（修改用户状态）
    @Transactional
    @Override
    public void updateStatus() {
        Long userId = BaseContext.getCurrentId();
        int rows = userMapper.updateStatus(userId);

        if (rows != 1) {
            throw new BusinessException("用户不存在或账号已停用");
        }
    }
}
