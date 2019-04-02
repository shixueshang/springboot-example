package com.lhd.dubbo.controller;

import com.lhd.dubbo.service.HelloConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihongde on 2019/04/01.
 */
@RestController
public class HelloController {

    @Autowired
    HelloConsumerService helloConsumerService;

    @GetMapping("/testHello")
    public String hello(String name) {
        return helloConsumerService.sayHello(name);
    }
}
