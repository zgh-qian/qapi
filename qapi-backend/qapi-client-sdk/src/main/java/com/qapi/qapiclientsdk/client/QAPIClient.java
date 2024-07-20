package com.qapi.qapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.qapi.qapiclientsdk.QAPIClientConfig;
import com.qapi.qapiclientsdk.model.InterfaceInfo;
import com.qapi.qapiclientsdk.model.User;
import com.qapi.qapiclientsdk.utils.SignUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * QAPI客户端
 */
public class QAPIClient {
    /**
     * appId
     */
    //private String appId;
    /**
     * appSecret
     */
    //private String appSecret;
    /**
     * userId
     */
    //private String userId;

    private String accessKey;
    private String secretKey;

    private static final String GATEWAY_HOST = "http://localhost:8090";

    public QAPIClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    /**
     * 获取请求头
     *
     * @param body 请求体
     * @return 请求头
     */
    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("accessKey", "qapi");
        headerMap.put("nonce", RandomUtil.randomNumbers(4));
        headerMap.put("body", body);
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headerMap.put("sign", SignUtils.genSign(body, secretKey));
        return headerMap;
    }

    public String getNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .charset(StandardCharsets.UTF_8)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
        System.out.println(body);
        return body;
    }

    /**
     * 调用接口
     *
     * @param interfaceInfoObj 接口信息
     * @return 接口返回值
     */
    public Object invokeByInterfaceInfo(Object interfaceInfoObj) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoObj, interfaceInfo);
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        // 如果是启用的，调用用户数据
        String requestParams = interfaceInfo.getRequestParams();
        // 如果是未启用的，调用模拟数据
        String mockParams = interfaceInfo.getMockParams();
        return null;
    }
}
