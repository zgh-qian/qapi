package com.qapi.qapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;


/**
 * 签名工具类
 */
public class SignUtils {
    /**
     * 获取签名
     *
     * @param body 请求体
     * @param secretKey 密钥
     * @return 签名
     */
    public static String genSign(String body, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body+ "." + secretKey;
        return digester.digestHex(content);
    }
}
