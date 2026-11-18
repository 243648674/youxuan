package com.xiangmu.youxuan.controller.user;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.user.UserDTO;
import com.xiangmu.youxuan.dto.user.UserLoginDTO;
import com.xiangmu.youxuan.service.UserService;
import com.xiangmu.youxuan.vo.userVO.UUserLoginVO;
import com.xiangmu.youxuan.vo.userVO.UUserVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UUserController {
    private final UserService userService;
    // 注入用户业务组件，供当前Controller的方法调用
    public UUserController(UserService userService){
        this.userService=userService;
    }
    // 校验用户登录信息和账号状态，返回用户信息及登录令牌
    @PostMapping("/userLogin")
    public Result<UUserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO){
        UUserLoginVO userLoginVO = userService.userLogin(userLoginDTO);
        return Result.success(userLoginVO);
    }
    // 注册用户账号，将编码后的密码保存到数据库
    @PostMapping("/insert")
    public Result insert(@RequestBody UserDTO userDTO){
        userService.insert(userDTO);
        return Result.success(null);
    }
    // 用户查询当前登录账号的个人信息
    @GetMapping("/selectMy")
    public Result<UUserVO> selectMy() {
        return Result.success(userService.userSelectMy());
    }
    // 用户停用当前登录账号，将账号状态由1改为0
    @PutMapping("/updateStatus")
    public Result updateStatus(){
        userService.updateStatus();
        return Result.success(null);
    }
}
