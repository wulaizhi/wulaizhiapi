package com.wulaizhi.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.wulaizhi.apiclientsdk.model.User;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import static com.wulaizhi.apiclientsdk.utils.SignUtils.genSign;

@Data
public class ApiClient {

    private  String accessKey;

    private String secretKey;

    private String url;

    private String salt = "saltwulaizhi";

    public ApiClient(String accessKey, String secretKey,String salt) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.salt=salt;
    }

    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result1= HttpRequest.get(url)
                .charset("UTF-8").form(paramMap).addHeaders(getHeaderMap(salt))
                .execute().body();
        return result1;
    }
    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpRequest.post(url)
                .charset("UTF-8").form(paramMap).addHeaders(getHeaderMap(salt))
                .execute().body();
        return result;
    }
    public String getNameByJson(String user) {
        String result2 = HttpRequest.post(url)
                .charset("UTF-8").body(user).addHeaders(getHeaderMap(salt))
                .execute().body();
        return result2;
    }
    private Map<String,String> getHeaderMap(String body) {
        Map<String,String> hashmap = new HashMap<>();
        hashmap.put("accessKey",accessKey);
        hashmap.put("nonce", RandomUtil.randomNumbers(3));
        hashmap.put("body",body);
        hashmap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        hashmap.put("sign",genSign(body,secretKey));
        return hashmap;
    }

}
