package com.lhd.sba;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lihongde on 2018/06/15.
 */
@SpringBootApplication
@EnableAdminServer
public class SBAApplication {

    public static void main(String[] args) {
        SpringApplication.run(SBAApplication.class, args);
    }
}
