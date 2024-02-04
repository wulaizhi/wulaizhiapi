package com.wulaizhi.springbootinit.service;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class InterfaceInfoServiceTest {

    @Resource
    InterfaceInfoService interfaceInfoService;
    @Test
    public void testAnalysisInterfaceInvoke() {
        System.out.println(interfaceInfoService.analysisInterfaceInvoke(2));
    }
}
