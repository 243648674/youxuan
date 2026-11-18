package com.xiangmu.youxuan.controller.employee;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.aftersale.AftersalePageQueryDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersaleUpdateDTO;
import com.xiangmu.youxuan.service.AftersaleService;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EAftersaleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/aftersale")
@RequiredArgsConstructor
public class EAftersaleController {
    private final AftersaleService aftersaleService;

    // 员工处理售后状态，退款完成时将订单标记为已退款并恢复库存
    @PutMapping("/update")
    public Result update(@RequestBody AftersaleUpdateDTO aftersaleUpdateDTO) {
        aftersaleService.update(aftersaleUpdateDTO);
        return Result.success(null);
    }

    // 处理员工关闭本人跟进的待处理或已同意售后单
    @PutMapping("/updateDelete")
    public Result updateDelete(@RequestParam Long id) {
        aftersaleService.updateDelete(id);
        return Result.success(null);
    }

    // 员工按筛选条件分页查询售后单，返回本页售后单和总条数
    @GetMapping("/select")
    public Result<EPageQueryVO<EAftersaleVO>> select(
            AftersalePageQueryDTO aftersalePageQueryDTO) {
        EPageQueryVO<EAftersaleVO> ePageQueryVO =
                aftersaleService.employeeSelect(aftersalePageQueryDTO);
        return Result.success(ePageQueryVO);
    }

}
