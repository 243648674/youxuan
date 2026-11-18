package com.xiangmu.youxuan.controller.employee;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.employee.EmployeeDTO;
import com.xiangmu.youxuan.dto.employee.EmployeeLoginDTO;
import com.xiangmu.youxuan.dto.employee.EmployeePageQueryDTO;
import com.xiangmu.youxuan.service.EmployeeService;
import com.xiangmu.youxuan.vo.employeeVO.EEmployeeLoginVO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EEmployeeController {
    private final EmployeeService employeeService;
    // 注入员工业务组件，供当前Controller的方法调用
    public EEmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }
    // 管理员创建员工账号，将编码后的密码保存到数据库
    @PostMapping("/insert")
    public Result insert(@RequestBody EmployeeDTO employeeDTO){
        employeeService.insert(employeeDTO);
        return Result.success(null);
    }
    // 校验员工登录信息和账号状态，返回员工信息及登录令牌
    @PostMapping("/employeeLogin")
    public Result<EEmployeeLoginVO> employeeLogin(@RequestBody EmployeeLoginDTO employeeLoginDTO){
       EEmployeeLoginVO employeeLoginVO = employeeService.employeeLogin(employeeLoginDTO);
       return Result.success(employeeLoginVO);
    }
    // 管理员修改普通员工的账号状态
    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestBody EmployeeDTO employeeDTO){
        employeeService.updateStatus(employeeDTO);
        return Result.success(null);
    }
    // 当前管理员将管理员身份转交给指定的启用普通员工
    @PutMapping("/transferAdmin")
    public Result transferAdmin(@RequestParam Long targetEmployeeId){
        employeeService.transferAdmin(targetEmployeeId);
        return Result.success(null);
    }
    // 员工修改指定员工的姓名、年龄、性别、手机号和身份证号
    @PutMapping("/update")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        employeeService.update(employeeDTO);
        return Result.success(null);
    }
    // 员工按筛选条件分页查询员工，返回本页员工和总条数
    @GetMapping("/select")
    public Result<EPageQueryVO> select(EmployeePageQueryDTO employeePageQueryDTO){
        EPageQueryVO pageResultVO = employeeService.select(employeePageQueryDTO);
        return Result.success(pageResultVO);
    }



}
