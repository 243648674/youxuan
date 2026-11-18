package com.xiangmu.youxuan.controller.employee;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.user.UserPageQueryDTO;
import com.xiangmu.youxuan.service.UserService;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/user")
@RequiredArgsConstructor
public class EUserController {
    private final UserService userService;

    // 员工按筛选条件分页查询用户，返回本页用户和总条数
    @GetMapping("/select")
    public Result<EPageQueryVO<EUserVO>> select(UserPageQueryDTO userPageQueryDTO) {
        EPageQueryVO<EUserVO> ePageQueryVO =
                userService.employeeSelect(userPageQueryDTO);
        return Result.success(ePageQueryVO);
    }
}
