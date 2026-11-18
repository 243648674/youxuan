package com.xiangmu.youxuan.controller.user;

import com.xiangmu.youxuan.common.Result;
import com.xiangmu.youxuan.dto.voucher.UserVoucherQueryDTO;
import com.xiangmu.youxuan.service.VoucherService;
import com.xiangmu.youxuan.vo.userVO.UQueryListVO;
import com.xiangmu.youxuan.vo.userVO.UUserVoucherVO;
import com.xiangmu.youxuan.vo.userVO.UVoucherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/voucher")
@RequiredArgsConstructor
public class UVoucherController {
    private final VoucherService voucherService;

    // 用户查询当前可免费领取的满减券和打折券
    @GetMapping("/select")
    public Result<List<UVoucherVO>> select() {
        List<UVoucherVO> vouchers = voucherService.userListSelect();
        return Result.success(vouchers);
    }

    // 用户按使用状态和券类型查询自己已领取的优惠券
    @GetMapping("/selectMy")
    public Result<UQueryListVO<UUserVoucherVO>> selectMy(UserVoucherQueryDTO userVoucherQueryDTO){
        UQueryListVO<UUserVoucherVO> userVoucherVOS =
                voucherService.userSelectMy(userVoucherQueryDTO);
        return Result.success(userVoucherVOS);
    }

    // 用户领取优惠券
    @PostMapping("/receive")
    public Result<Void> receive(@RequestParam("voucherId") Long voucherId) {
        voucherService.receive(voucherId);
        return Result.success(null);
    }
}
