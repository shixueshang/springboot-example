package com.lhd.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhd.dubbo.service.HelloService;

/**
 * Created by lihongde on 2019/04/01.
 */
@Service(version = "1.0.0")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return  "Hello , " + name;
    }
}
