package com.qapi.qapigateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import com.qapi.qapibackend.model.entity.User;
import com.qapi.qapibackend.provider.InnerInterfaceInfoService;
import com.qapi.qapibackend.provider.InnerUserInterfaceInfoService;
import com.qapi.qapibackend.provider.InnerUserService;
import com.qapi.qapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 全局过滤器
 */
@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserService userService;

    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService userInterfaceInfoService;

    // 白名单
    private static final List<String> IP_WHITE_LIST = Collections.singletonList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8110";

    /**
     * 过滤器逻辑
     *
     * @param exchange 请求
     * @param chain    过滤器链
     * @return 过滤器结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 请求日志打印
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().toString();
        String method = request.getMethod().toString();
        log.info("请求标识：{}", request.getId());
        log.info("请求方法：{}", method);
        log.info("请求路径：{}", path);
        log.info("请求参数：{}", request.getQueryParams());
        String hostString = request.getLocalAddress().getHostString();
        log.info("请求来源地址：{}", request.getLocalAddress().getHostString());
        log.info("请求来源地址：{}", request.getRemoteAddress());
        // 响应体
        ServerHttpResponse response = exchange.getResponse();
        // 2. 访问控制，黑白名单校验
        if (!IP_WHITE_LIST.contains(hostString)) {
            log.error("请求来源地址不在白名单中，请求已被拒绝！");
            return handleNoAuth(response);
        }
        // 3. 用户鉴权（判断 accessKey secretKey 是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // todo 数据库查询
        User invokeUser = null;
        try {
            invokeUser = userService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error:", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        /*
        // 测试
        if (!accessKey.equals("qapi")) {
            return handleNoAuth(response);
        }*/
        // 随机数不能超过10000
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        // 时间和当前时间不能超过5分钟
        Long FIVE_MINUTE = 5 * 60L;
        if ((System.currentTimeMillis() / 1000 - Long.parseLong(timestamp)) > FIVE_MINUTE) {
            throw new RuntimeException("Invalid timestamp");
        }
        // todo 签名验证，实际应该从数据库中查询secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // 4. 请求的模拟接口是否存在
        // todo 数据库查询模拟接口是否存在，请求方法是否匹配，校验请求参数是否合法
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = interfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error:", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        // 5. 请求转发，调用模拟接口
        return handleResponse(exchange, chain, interfaceInfo, invokeUser);
    }

    /**
     * 响应处理
     *
     * @param exchange 请求
     * @param chain    过滤器链
     * @return 过滤器结果
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, InterfaceInfo interfaceInfo, User invokeUser) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 等调用的接口执行完才执行
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer buff = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[buff.readableByteCount()];
                            buff.read(content);
                            DataBufferUtils.release(buff);//释放掉内存
                            // 排除Excel导出，不是application/json不打印。若请求是上传图片则在最上面判断。
                            MediaType contentType = originalResponse.getHeaders().getContentType();
                            HttpStatus returnCode = getDelegate().getStatusCode();
                            // 获取内容并转换成可读的形式
                            String contentStr = new String(content, StandardCharsets.UTF_8);
                            log.info("响应内容:{}", contentStr);
                            log.info("响应码:{}", returnCode);
                            // todo 7. 调用成功，接口调用次数 + 1
                            try {
                                boolean invokeCount = userInterfaceInfoService.invokeCount(interfaceInfo.getId(), invokeUser.getId());
                                System.out.println(invokeCount);
                            } catch (Exception e) {
                                log.error("invokeCount error:", e);
                            }
                            // todo 8. 接口调用次数 + 1
                            if (!MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                                return bufferFactory.wrap(content);
                            }
                            // 构建返回日志
                            String joinData = new String(content);
                            String result = modifyBody(joinData);
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode().value());
                            rspArgs.add(exchange.getRequest().getURI());
                            rspArgs.add(result);
                            log.info("<-- {} {}\n{}", rspArgs.toArray());
                            getDelegate().getHeaders().setContentLength(result.getBytes().length);

                            return bufferFactory.wrap(result.getBytes());
                        }));
                    } else {
                        log.error("<-- {} 响应code异常", getStatusCode());
                    }
                    //return super.writeWith(body);
                    // 8. 调用失败，返回错误码
                    return handleInvokeError(originalResponse);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        } catch (Exception e) {
            log.error("网关响应异常：" + e);
            return chain.filter(exchange);
        }
    }

    /**
     * 过滤器顺序，越小越先执行
     *
     * @return 过滤器顺序
     */
    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 处理未授权的请求
     *
     * @param response 服务器响应
     * @return 服务器响应
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * 处理接口调用失败的请求
     *
     * @param response 服务器响应
     * @return 服务器响应
     */
    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    //返回统一的JSON日期数据 2024-02-23 11:00， null转空字符串
    private String modifyBody(String jsonStr) {
        JSONObject json = JSON.parseObject(jsonStr, Feature.AllowISO8601DateFormat);
        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
        return JSONObject.toJSONString(json, (ValueFilter) (object, name, value) -> value == null ? "" : value, SerializerFeature.WriteDateUseDateFormat);
    }
}
