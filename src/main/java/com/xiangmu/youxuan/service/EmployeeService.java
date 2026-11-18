package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.dto.employee.EmployeeDTO;
import com.xiangmu.youxuan.dto.employee.EmployeeLoginDTO;
import com.xiangmu.youxuan.dto.employee.EmployeePageQueryDTO;
import com.xiangmu.youxuan.vo.employeeVO.EEmployeeLoginVO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;

public interface EmployeeService {
    // 员工端：管理员创建员工账号，将编码后的密码保存到数据库
    void insert(EmployeeDTO employeeDTO);

    // 员工端：校验员工登录信息和账号状态，返回员工信息及登录令牌
    EEmployeeLoginVO employeeLogin(EmployeeLoginDTO employeeLoginDTO);

    // 员工端：管理员修改普通员工的账号状态
    void updateStatus(EmployeeDTO employeeDTO);

    // 员工端：当前管理员将管理员身份转交给指定的启用普通员工
    void transferAdmin(Long targetEmployeeId);

    // 员工端：员工修改指定员工的姓名、年龄、性别、手机号和身份证号
    void update(EmployeeDTO employeeDTO);

    // 员工端：员工按筛选条件分页查询员工，返回本页员工和总条数
    EPageQueryVO select(EmployeePageQueryDTO employeePageQueryDTO);
}
