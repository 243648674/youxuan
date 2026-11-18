package com.xiangmu.youxuan.service;

import com.xiangmu.youxuan.dto.aftersale.AftersaleDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersalePageQueryDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersaleUpdateDTO;
import com.xiangmu.youxuan.vo.employeeVO.EAftersaleVO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.userVO.UAftersaleVO;

import java.util.List;

public interface AftersaleService {
    // 用户端：用户为本人订单申请售后，由服务端确定申请人、金额和初始状态
    void insert(AftersaleDTO aftersaleDTO);

    // 员工端：员工处理售后状态，退款完成时将订单标记为已退款并恢复库存
    void update(AftersaleUpdateDTO aftersaleUpdateDTO);

    // 用户端/员工端：用户取消本人的售后，或处理员工关闭本人跟进的售后，仅允许状态0或1
    void updateDelete(Long id);

    // 用户端：用户确认本人已获员工同意的投诉售后处理完成，将状态由1改为4
    void confirmComplete(Long id);

    // 员工端：员工按筛选条件分页查询售后单，返回本页售后单和总条数
    EPageQueryVO<EAftersaleVO> employeeSelect(AftersalePageQueryDTO aftersalePageQueryDTO);

    // 用户端：用户查询当前登录账号申请的全部售后单
    List<UAftersaleVO> userSelectMy();
}
