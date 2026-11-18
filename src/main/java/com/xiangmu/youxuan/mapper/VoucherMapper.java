package com.xiangmu.youxuan.mapper;

import com.xiangmu.youxuan.dto.voucher.VoucherPageQueryDTO;
import com.xiangmu.youxuan.dto.voucher.UserVoucherQueryDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherUpdateDTO;
import com.xiangmu.youxuan.entity.Voucher;
import com.xiangmu.youxuan.vo.userVO.UUserVoucherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoucherMapper {
    //新增优惠券，初始状态设为启用
    void insert(VoucherDTO voucherDTO);
    //修改传入的优惠规则、库存和时间
    void update(VoucherUpdateDTO voucherUpdateDTO);
    //修改优惠券状态
    void updateStatus(@Param("id") Long id,@Param("status") Integer status);
    //删除已停用的优惠券
    int delete(@Param("id") Long id);
    //员工分页查询优惠券
    List<Voucher> select(@Param("begin") Long begin,
                         @Param("voucherPageQueryDTO") VoucherPageQueryDTO voucherPageQueryDTO);
    Long count(VoucherPageQueryDTO voucherPageQueryDTO);
    //根据ID查询优惠券
    Voucher selectById(@Param("id") Long id);
    //根据优惠券标题查询
    Voucher selectByTitle(@Param("title") String title);
    //修改前锁定优惠券，避免并发修改时覆盖其他请求的字段
    Voucher selectByIdForUpdate(@Param("id") Long id);
    //用户查询当前可以免费领取的优惠券
    List<Voucher> userListSelect();
    //查询本人的领券记录及对应优惠规则
    List<UUserVoucherVO> selectUserList(@Param("userId") Long userId,
            @Param("userVoucherQueryDTO") UserVoucherQueryDTO userVoucherQueryDTO);
    //统计优惠券已有的领取记录，删除前检查
    Long countUserVoucherByVoucherId(@Param("voucherId") Long voucherId);
    //领取优惠券时扣减数据库库存
    int receiveStock(@Param("voucherId") Long voucherId);
    //保存用户领券记录
    int insertUserVoucher(@Param("userId") Long userId,
                          @Param("voucherId") Long voucherId);
}
