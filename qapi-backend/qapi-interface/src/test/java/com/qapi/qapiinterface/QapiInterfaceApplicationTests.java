package com.qapi.qapiinterface;

import com.qapi.qapiclientsdk.client.QAPIClient;
import com.qapi.qapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class QapiInterfaceApplicationTests {

    @Resource
    private QAPIClient qAPIClient;

    @Test
    void sdk() {
        String result1 = qAPIClient.getNameByGet("zgh");
        User user = new User();
        user.setUsername("zgh");
        String result2 = qAPIClient.getNameByPost(user);
        System.out.println(result1);
        System.out.println(result2);
    }

}
