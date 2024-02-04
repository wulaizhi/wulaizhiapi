package com.wulaizhi.apibinterface.controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import com.wulaizhi.apiclientsdk.model.User;
@RestController
@RequestMapping("/publicInterface")
public class publicInterface {

    @GetMapping("/get")
    public String getNameByGet(@RequestParam("name") String name){
        return "GET调用名称:"+name;
    }
    @PostMapping("/post")
    public String getNameByPost(String name) {
        return "POST调用名称:"+name;
    }
    @PostMapping("/json")
    public String getNameByJson(@RequestBody User user, HttpServletRequest request) throws UnsupportedEncodingException {
        return "JSON调用名称:"+user.getName();
    }
}
