package com.wulaizhi.apiclientsdk;

import com.wulaizhi.apiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("wulaizhi.client")
@Data
@ComponentScan
public class wulaizhiClientConfig {
     private  String accessKey;
     private  String secretKey;
     private String salt;
     @Bean
    public ApiClient apiClient() {
         return new ApiClient(accessKey,secretKey,salt);
     }
}
