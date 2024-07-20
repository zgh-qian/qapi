package com.qapi.qapibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qapi.qapibackend.annotation.AuthCheck;
import com.qapi.qapibackend.common.BaseResponse;
import com.qapi.qapibackend.common.ResultUtils;
import com.qapi.qapibackend.constant.UserConstant;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import com.qapi.qapibackend.model.enums.InterfaceInfoStatusEnum;
import com.qapi.qapibackend.model.vo.analysis.InterfaceInfoAnalysisVO;
import com.qapi.qapibackend.service.InterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
//@Api(tags = "Analysis")
public class AnalysisController {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 获取接口调用次数最多的接口top
     *
     * @param limit 接口数量
     * @return top接口列表
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoAnalysisVO>> listTopInvokeInterface(@RequestParam("limit") Integer limit) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("status", InterfaceInfoStatusEnum.ONLINE.getValue())
                .orderByDesc("call_count")
                .last("limit " + limit);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        List<InterfaceInfoAnalysisVO> interfaceInfoAnalysisVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoAnalysisVO interfaceInfoAnalysisVO = new InterfaceInfoAnalysisVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoAnalysisVO);
            return interfaceInfoAnalysisVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoAnalysisVOList);
    }
}
