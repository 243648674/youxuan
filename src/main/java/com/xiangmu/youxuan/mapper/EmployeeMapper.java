package com.xiangmu.youxuan.mapper;

import com.xiangmu.youxuan.dto.employee.EmployeeDTO;
import com.xiangmu.youxuan.dto.employee.EmployeePageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import com.xiangmu.youxuan.entity.Employee;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    // 根据登录用户名从数据库查询员工账号，包含密码编码值
    Employee employeeSelectByUsername(String username);
    // 向数据库新增员工账号，将账号初始状态设为启用
    void insert(EmployeeDTO employeeDTO);
    // 修改指定普通员工的账号状态，返回影响行数
    int updateStatus(EmployeeDTO employeeDTO);
    // 按员工ID和旧类型修改启用员工的类型，返回影响行数
    int updateEmployeeType(@Param("id") Long id,
                           @Param("oldType") Integer oldType,
                           @Param("newType") Integer newType);
    // 根据员工ID更新姓名、年龄、性别、手机号和身份证号
    void update(EmployeeDTO employeeDTO);
    // 根据员工ID从数据库查询员工资料
    Employee selectById(Long id);
    // 按筛选条件和分页范围从数据库查询本页员工列表
    List<Employee> select(@Param("begin") Integer begin,@Param("employeePageQueryDTO") EmployeePageQueryDTO employeePageQueryDTO);
    // 按筛选条件统计数据库中的员工条数
    Long count(EmployeePageQueryDTO employeePageQueryDTO);
}
