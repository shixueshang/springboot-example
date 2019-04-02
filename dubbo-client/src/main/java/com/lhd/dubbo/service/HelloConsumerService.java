package com.lhd.dubbo.service;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * Created by lihongde on 2019/04/02.
 */
@Service
public class HelloConsumerService implements HelloService{

    @Reference(version = "1.0.0")
    HelloService helloService;

    @Override
    public String sayHello(String name) {
        return helloService.sayHello(name);
    }
}
