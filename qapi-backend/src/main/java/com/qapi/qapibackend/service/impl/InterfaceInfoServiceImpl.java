package com.qapi.qapibackend.service.impl;

import clover.com.google.gson.Gson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qapi.qapibackend.common.BaseContext;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.common.ErrorCode;
import com.qapi.qapibackend.common.IdRequest;
import com.qapi.qapibackend.constant.CommonConstant;
import com.qapi.qapibackend.exception.BusinessException;
import com.qapi.qapibackend.exception.ThrowUtils;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import com.qapi.qapibackend.model.entity.User;
import com.qapi.qapibackend.model.enums.InterfaceInfoStatusEnum;
import com.qapi.qapibackend.model.vo.interfaceInfo.InterfaceInfoVO;
import com.qapi.qapibackend.service.InterfaceInfoService;
import com.qapi.qapibackend.mapper.InterfaceInfoMapper;
import com.qapi.qapibackend.service.UserService;
import com.qapi.qapibackend.utils.InterfaceInfoUtils;
import com.qapi.qapibackend.utils.SqlUtils;
import com.qapi.qapiclientsdk.client.QAPIClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

import java.util.stream.Collectors;

import static com.qapi.qapibackend.constant.InterfaceInfoConstant.*;

@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {
    @Resource
    private UserService userService;
    // 系统的 QAPIClient
    @Resource
    private QAPIClient qapiClient;

    /**
     * 添加接口信息
     *
     * @param interfaceInfoAddRequest 接口信息
     */
    @Override
    public void addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        InterfaceInfo interfaceInfo = InterfaceInfoUtils.validateInterfaceInfo(interfaceInfoAddRequest, INTERFACE_INFO_CREATE_FAILURE);
        interfaceInfo.setUserId(BaseContext.getUserId());
        boolean save = this.save(interfaceInfo);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, INTERFACE_INFO_CREATE_FAILURE);
    }

    /**
     * 删除接口信息
     *
     * @param deleteRequest 删除请求
     */
    @Override
    public void deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_DELETE_FAILURE, INTERFACE_INFO_PARAM_NULL);
        }
        Long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo interfaceInfo = this.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, INTERFACE_INFO_DELETE_FAILURE, INTERFACE_INFO_NOT_EXIST);
        // 本人和管理员可以删除
        Long interfaceInfoUserId = interfaceInfo.getUserId();
        Long userId = BaseContext.getUserId();
        if (!(userId.equals(interfaceInfoUserId) || !userService.isAdmin())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, INTERFACE_INFO_DELETE_FAILURE, INTERFACE_INFO_DELETE_PERMISSION_DENIED);
        }
        boolean remove = this.removeById(id);
        ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR, INTERFACE_INFO_DELETE_FAILURE);
    }

    /**
     * 更新接口信息
     *
     * @param interfaceInfoUpdateRequest 接口信息
     */
    @Override
    public void updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        InterfaceInfo updateInterfaceInfo = InterfaceInfoUtils.validateInterfaceInfo(interfaceInfoUpdateRequest, INTERFACE_INFO_UPDATE_FAILURE);
        InterfaceInfo interfaceInfo = this.getById(updateInterfaceInfo.getId());
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, INTERFACE_INFO_UPDATE_FAILURE, INTERFACE_INFO_NOT_EXIST);
        // 本人和管理员可以更新
        Long interfaceInfoUserId = interfaceInfo.getUserId();
        Long userId = BaseContext.getUserId();
        if (!(userId.equals(interfaceInfoUserId) || !userService.isAdmin())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, INTERFACE_INFO_UPDATE_FAILURE, INTERFACE_INFO_UPDATE_PERMISSION_DENIED);
        }
        boolean update = this.updateById(updateInterfaceInfo);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, INTERFACE_INFO_UPDATE_FAILURE);
    }

    /**
     * 根据id获取接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    @Override
    public InterfaceInfoVO getInterfaceInfoVo(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_GET_FAILURE);
        }
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, INTERFACE_INFO_GET_FAILURE, INTERFACE_INFO_NOT_EXIST);
        }
        return InterfaceInfoVO.objToVO(interfaceInfo);
    }

    /**
     * 分页查询接口信息
     *
     * @param interfaceInfoQueryRequest 接口信息查询请求
     * @return 接口信息分页
     */
    @Override
    public Page<InterfaceInfoVO> listInterfaceInfoVoPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Page<InterfaceInfo> interfaceInfoPage = listInterfaceInfoPage(interfaceInfoQueryRequest);
        Page<InterfaceInfoVO> interfaceInfoVoPage = new Page<>();
        BeanUtils.copyProperties(interfaceInfoPage, interfaceInfoVoPage);
        interfaceInfoVoPage.setRecords(interfaceInfoPage.getRecords().stream().map(InterfaceInfoVO::objToVO).collect(Collectors.toList()));
        return interfaceInfoVoPage;
    }

    /**
     * 根据id获取接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    @Override
    public InterfaceInfo getInterfaceInfo(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_GET_FAILURE);
        }
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, INTERFACE_INFO_GET_FAILURE, INTERFACE_INFO_NOT_EXIST);
        }
        return interfaceInfo;
    }

    /**
     * 分页查询接口信息
     *
     * @param interfaceInfoQueryRequest 接口信息查询请求
     * @return 接口信息分页
     */
    @Override
    public Page<InterfaceInfo> listInterfaceInfoPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_LIST_FAILURE, INTERFACE_INFO_PARAM_NULL);
        }
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String url = interfaceInfoQueryRequest.getUrl();
        String method = interfaceInfoQueryRequest.getMethod();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        String description = interfaceInfoQueryRequest.getDescription();
        Integer callCount = interfaceInfoQueryRequest.getCallCount();
        Integer status = interfaceInfoQueryRequest.getStatus();
        Long userId = interfaceInfoQueryRequest.getUserId();
        int current = interfaceInfoQueryRequest.getCurrent();
        int pageSize = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (StringUtils.isNotBlank(url)) {
            queryWrapper.like("url", url);
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(method), "method", method);
        if (StringUtils.isNotBlank(requestHeader)) {
            queryWrapper.like("request_header", requestHeader);
        }
        if (StringUtils.isNotBlank(responseHeader)) {
            queryWrapper.like("response_header", responseHeader);
        }
        if (StringUtils.isNotBlank(description)) {
            queryWrapper.like("description", description);
        }
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        // 限制查询条件，防止恶意查询
        ThrowUtils.throwIf(pageSize >= 100, ErrorCode.PARAMS_ERROR);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    /**
     * 发布接口信息
     *
     * @param idRequest 发布请求
     */
    @Override
    public void onlineInterfaceInfo(IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_ONLINE_FAILURE, INTERFACE_INFO_PARAM_NULL);
        }
        // 判断是否存在
        Long id = idRequest.getId();
        InterfaceInfo interfaceInfo = this.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, INTERFACE_INFO_ONLINE_FAILURE, INTERFACE_INFO_NOT_EXIST);
        // todo 根据接口地址等参数进行调用
        //Object result = qapiClient.invokeByInterfaceInfo(interfaceInfo);
        // todo 判断该接口是否可以调用
        com.qapi.qapiclientsdk.model.User user = new com.qapi.qapiclientsdk.model.User();
        user.setUsername("zgh");
        String result = qapiClient.getNameByPost(user);
        if (StringUtils.isBlank(result)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, INTERFACE_INFO_ONLINE_FAILURE, INTERFACE_INFO_OPRATION_FAILURE);
        }
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean updateById = this.updateById(interfaceInfo);
        ThrowUtils.throwIf(!updateById, ErrorCode.OPERATION_ERROR, INTERFACE_INFO_ONLINE_FAILURE);
    }

    /**
     * 下线接口信息
     *
     * @param idRequest 下线请求
     */
    @Override
    public void offlineInterfaceInfo(IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_ONLINE_FAILURE, INTERFACE_INFO_PARAM_NULL);
        }
        // 判断是否存在
        Long id = idRequest.getId();
        InterfaceInfo interfaceInfo = this.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, INTERFACE_INFO_ONLINE_FAILURE, INTERFACE_INFO_NOT_EXIST);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean updateById = this.updateById(interfaceInfo);
        ThrowUtils.throwIf(!updateById, ErrorCode.OPERATION_ERROR, INTERFACE_INFO_ONLINE_FAILURE);
    }

    /**
     * 调用接口信息
     *
     * @param interfaceInfoInvokeRequest 接口调用请求
     * @return 接口调用结果
     */
    @Override
    public Object invokeInterfaceInfo(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {
        if (interfaceInfoInvokeRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_INVOKE_FAILURE, INTERFACE_INFO_PARAM_NULL);
        }
        // 判断是否存在
        Long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        InterfaceInfo interfaceInfo = this.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, INTERFACE_INFO_INVOKE_FAILURE, INTERFACE_INFO_NOT_EXIST);
        // 判断接口状态
        if (!interfaceInfo.getStatus().equals(InterfaceInfoStatusEnum.ONLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_INVOKE_FAILURE, INTERFACE_INFO_STATUS_ERROR);
        }
        User loginUser = userService.getById(BaseContext.getUserId());
        Gson gson = new Gson();
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // 生成用户的qapi客户端
        QAPIClient userQAPIClient = new QAPIClient(accessKey, secretKey);
        // 生成请求参数
        com.qapi.qapiclientsdk.model.User user = gson.fromJson(userRequestParams, com.qapi.qapiclientsdk.model.User.class);
        String result = userQAPIClient.getNameByPost(user);
        if (StringUtils.isBlank(result)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, INTERFACE_INFO_INVOKE_FAILURE, INTERFACE_INFO_OPRATION_FAILURE);
        }
        return result;
    }
}




