package com.wulaizhi.apibinterface;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.wulaizhi.apiclientsdk.client.ApiClient;
@SpringBootTest
class ApiBInterfaceApplicationTests {

    @Autowired
    ApiClient apiClient;
    @Test
    void contextLoads() {
        apiClient.getNameByJson();
    }

}
