package com.qapi.qapiinterface.controller;

import com.qapi.qapiclientsdk.QAPIClientConfig;
import com.qapi.qapiclientsdk.model.User;
import com.qapi.qapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {
    @Resource
    private QAPIClientConfig qAPIClientConfig;

    @GetMapping("/")
    public String getNameByGet(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("qapi"));
        return "Get method, your name is " + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "Post method, your name is " + name;
    }

    @PostMapping("/user")
    public String getNameByPost(@RequestBody User user, HttpServletRequest request) {
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        // todo 数据库查询是否有该用户 userId
        // todo 随机数不能超过10000
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("Invalid nonce");
        }
        // todo 时间和当前时间不能超过5分钟
        if (System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > 300) {
            throw new RuntimeException("Invalid timestamp");
        }
        // todo 签名验证，实际应该从数据库中查询secretKey
        String serverSign = SignUtils.genSign(body, qAPIClientConfig.getSecretKey());
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("Invalid sign");
        }
        return "Post method, username is " + user.getUsername();
    }
}