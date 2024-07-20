package com.qapi.qapiclientsdk;

import com.qapi.qapiclientsdk.client.QAPIClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("qapi.client")
@Data
@ComponentScan
public class QAPIClientConfig {
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

    @Bean
    public QAPIClient qAPIClient() {
        return new QAPIClient(accessKey, secretKey);
    }
}
