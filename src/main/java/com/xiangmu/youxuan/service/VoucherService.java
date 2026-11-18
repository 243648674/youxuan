package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.dto.voucher.VoucherDTO;
import com.xiangmu.youxuan.dto.voucher.UserVoucherQueryDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherPageQueryDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherStatusDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherUpdateDTO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EVoucherVO;
import com.xiangmu.youxuan.vo.userVO.UVoucherVO;
import com.xiangmu.youxuan.vo.userVO.UQueryListVO;
import com.xiangmu.youxuan.vo.userVO.UUserVoucherVO;

import java.util.List;

public interface VoucherService {

    // 员工端：新增免费领取的满减券或打折券
    void insert(VoucherDTO voucherDTO);

    // 员工端：修改优惠规则、可领取库存和领取、使用时间，券类型创建后不变
    void update(VoucherUpdateDTO voucherUpdateDTO);

    // 员工端：修改指定优惠券的启用或停用状态
    void updateStatus(VoucherStatusDTO voucherStatusDTO);

    // 员工端：删除已停用且没有领取记录的优惠券
    void delete(Long id);

    // 员工端：按状态和类型分页查询基础信息，返回总数和本页列表
    EPageQueryVO<EVoucherVO> employeeSelect(VoucherPageQueryDTO voucherPageQueryDTO);

    // 员工端：根据优惠券ID查询完整信息
    EVoucherVO employeeSelectById(Long id);

    // 用户端：查询当前处于领取时间内、已启用且有库存的优惠券
    List<UVoucherVO> userListSelect();

    // 用户端：按使用状态和券类型查询本人的领券记录，返回列表和条数
    UQueryListVO<UUserVoucherVO> userSelectMy(UserVoucherQueryDTO userVoucherQueryDTO);

    //用户领取优惠券库存预扣除（秒杀）
    void receive(Long voucherId);

    //用户创建优惠券订单
    void createUserVoucher(Long userId,Long voucherId);
}
