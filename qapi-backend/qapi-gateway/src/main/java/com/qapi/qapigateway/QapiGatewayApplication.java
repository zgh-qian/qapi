package com.qapi.qapigateway;

import com.qapi.qapibackend.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDubbo
public class QapiGatewayApplication {
    /*public static void main(String[] args) {
        SpringApplication.run(QapiGatewayApplication.class, args);
    }*/
    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(QapiGatewayApplication.class, args);
        QapiGatewayApplication application = context.getBean(QapiGatewayApplication.class);
        String result = application.sayHello("world");
        System.out.println(result);
    }

    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}
