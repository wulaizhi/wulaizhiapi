package com.wulaizhi.springbootinit.service;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserInterfaceInfoServiceTest {

    @Resource
    InterfaceInfoService interfaceInfoService;
    @Test
    public void testAnalysisInterfaceInvoke() {
    }
}
