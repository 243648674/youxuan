package com.xiangmu.youxuan.controller.user;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.aftersale.AftersaleDTO;
import com.xiangmu.youxuan.service.AftersaleService;
import com.xiangmu.youxuan.vo.userVO.UAftersaleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/aftersale")
@RequiredArgsConstructor
public class UAftersaleController {
    private final AftersaleService aftersaleService;
    // 用户为本人订单申请售后，由服务端确定申请人、金额和初始状态
    @PostMapping("/insert")
    public Result insert(@RequestBody AftersaleDTO aftersaleDTO){
        aftersaleService.insert(aftersaleDTO);
        return Result.success(null);
    }
    // 用户确认本人已获员工同意的投诉售后处理完成，将状态由1改为4
    @PutMapping("/confirmComplete")
    public Result confirmComplete(@RequestParam Long id) {
        aftersaleService.confirmComplete(id);
        return Result.success(null);
    }
    // 用户取消本人的待处理或已同意售后单
    @PutMapping("/updateDelete")
    public Result updateDelete(@RequestParam Long id){
        aftersaleService.updateDelete(id);
        return Result.success(null);
    }
    // 用户查询当前登录账号申请的全部售后单
    @GetMapping("/selectMy")
    public Result<List<UAftersaleVO>> selectMy(){
        List<UAftersaleVO> aftersaleVOS = aftersaleService.userSelectMy();
        return Result.success(aftersaleVOS);
    }
}
