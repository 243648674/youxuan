package com.xiangmu.youxuan.service.impl;
import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.config.PasswordConfig;
import com.xiangmu.youxuan.context.BaseContext;
import com.xiangmu.youxuan.dto.employee.EmployeeDTO;
import com.xiangmu.youxuan.dto.employee.EmployeeLoginDTO;
import com.xiangmu.youxuan.dto.employee.EmployeePageQueryDTO;
import com.xiangmu.youxuan.entity.Employee;
import com.xiangmu.youxuan.mapper.EmployeeMapper;
import com.xiangmu.youxuan.service.EmployeeService;
import com.xiangmu.youxuan.utils.JwtUtil;
import com.xiangmu.youxuan.vo.employeeVO.EEmployeeLoginVO;
import com.xiangmu.youxuan.vo.employeeVO.EEmployeeVO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final PasswordConfig passwordConfig;

    //校验当前登录员工是否为管理员
    private void employeeAdmin() {
        Long employeeId = BaseContext.getCurrentId();
        Employee employee = employeeMapper.selectById(employeeId);
        if (employee == null) {
            throw new BusinessException("当前员工不存在");
        }
        if (!Objects.equals(employee.getEmployeeType(), 0)) {
            throw new BusinessException("只有管理员可以执行该操作");
        }
    }

    //新增员工
    @Transactional
    @Override
    public void insert(EmployeeDTO employeeDTO){
        employeeAdmin();
        Employee employee = employeeMapper.employeeSelectByUsername(employeeDTO.getUsername());
        if (employee != null){
            throw new BusinessException("员工用户名已存在");
        }
        employeeDTO.setPassword(passwordConfig.passwordEncoder().encode(employeeDTO.getPassword()));
        employeeMapper.insert(employeeDTO);
    }
    //员工登录
    @Override
    public EEmployeeLoginVO employeeLogin(EmployeeLoginDTO employeeLoginDTO){
        Employee employee = employeeMapper.employeeSelectByUsername(employeeLoginDTO.getUsername());
        if (employee == null ){
            throw new BusinessException("员工账号不存在");
        }
        if (!passwordConfig.passwordEncoder().matches(employeeLoginDTO.getPassword(),employee.getPassword())){
            throw new BusinessException("密码错误");
        }
        if (employee.getStatus() == 0){
            throw new BusinessException("员工账号被封禁");
        }
        String token = JwtUtil.createEmployeeJWT(employee.getId());
        EEmployeeLoginVO employeeLoginVO = new EEmployeeLoginVO();
        BeanUtils.copyProperties(employee,employeeLoginVO);
        employeeLoginVO.setToken(token);
        return employeeLoginVO;
    }
    //删除员工（将员工状态设为0）
    @Transactional
    @Override
    public void updateStatus(EmployeeDTO employeeDTO) {
        employeeAdmin();

        int rows = employeeMapper.updateStatus(employeeDTO);
        if (rows != 1) {
            throw new BusinessException("员工不存在或不是普通员工，不能修改状态");
        }
    }
    //转让管理员权限
    @Transactional
    @Override
    public void transferAdmin(Long targetEmployeeId) {
        Long employeeId = BaseContext.getCurrentId();
        if (Objects.equals(employeeId, targetEmployeeId)) {
            throw new BusinessException("不能将管理员权限转让给自己");
        }
        int rows = employeeMapper.updateEmployeeType(
                employeeId, 0, 1);
        if (rows != 1) {
            throw new BusinessException("管理员身份已发生变化，请重新操作");
        }

        int rows1 = employeeMapper.updateEmployeeType(
                targetEmployeeId, 1, 0);
        if (rows1 != 1) {
            throw new BusinessException("目标员工状态已发生变化，请重新操作");
        }

    }
    //员工信息修改
    @Transactional
    @Override
    public void update(EmployeeDTO employeeDTO){
        Employee employee = employeeMapper.selectById(employeeDTO.getId());
        if (employee == null){
            throw new BusinessException("不存在该员工");
        }
        employeeMapper.update(employeeDTO);
    }
    //分页查询员工
    @Override
    public EPageQueryVO select(EmployeePageQueryDTO employeePageQueryDTO){
        Integer begin = (employeePageQueryDTO.getPage()-1)*employeePageQueryDTO.getPageSize();
        Long count = employeeMapper.count(employeePageQueryDTO);
        List<Employee> employees = employeeMapper.select(begin,employeePageQueryDTO);
        List<EEmployeeVO> employeeVOS = new ArrayList<>();
        for (Employee employee : employees) {
            EEmployeeVO employeeVO = new EEmployeeVO();
            BeanUtils.copyProperties(employee,employeeVO);
            employeeVOS.add(employeeVO);
        }
        EPageQueryVO<EEmployeeVO> ePageQueryVO = new EPageQueryVO<>();
        ePageQueryVO.setCount(count);
        ePageQueryVO.setPageQueryVO(employeeVOS);
        return ePageQueryVO;
    }
}
