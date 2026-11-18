package com.xiangmu.youxuan.controller.employee;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.voucher.VoucherDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherPageQueryDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherStatusDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherUpdateDTO;
import com.xiangmu.youxuan.service.VoucherService;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EVoucherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/voucher")
@RequiredArgsConstructor
public class EVoucherController {

    private final VoucherService voucherService;

    // 新增优惠券
    @PostMapping("/insert")
    public Result<Void> insert(@RequestBody VoucherDTO voucherDTO) {
        voucherService.insert(voucherDTO);
        return Result.success(null);
    }

    // 修改优惠券基础资料
    @PutMapping("/update")
    public Result<Void> update(@RequestBody VoucherUpdateDTO voucherUpdateDTO) {
        voucherService.update(voucherUpdateDTO);
        return Result.success(null);
    }

    // 修改优惠券启用状态
    @PutMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestBody VoucherStatusDTO voucherStatusDTO) {
        voucherService.updateStatus(voucherStatusDTO);
        return Result.success(null);
    }

    // 删除优惠券
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        voucherService.delete(id);
        return Result.success(null);
    }

    // 员工分页查询优惠券基础信息和总数
    @GetMapping("/select")
    public Result<EPageQueryVO<EVoucherVO>> select(VoucherPageQueryDTO voucherPageQueryDTO) {
        EPageQueryVO<EVoucherVO> pageQueryVO = voucherService.employeeSelect(voucherPageQueryDTO);
        return Result.success(pageQueryVO);
    }

    // 根据优惠券ID查询完整信息
    @GetMapping("/selectById")
    public Result<EVoucherVO> selectById(@RequestParam Long id) {
        EVoucherVO voucherVO = voucherService.employeeSelectById(id);
        return Result.success(voucherVO);
    }
}
