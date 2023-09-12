package com.dasco.openhis;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.dasco.openhis.mapper"})
@EnableDubbo
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class,args);
        System.out.println("短信子系统启动成功");
    }
}
