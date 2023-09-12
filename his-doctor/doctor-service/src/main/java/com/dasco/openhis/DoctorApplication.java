package com.dasco.openhis;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = {"com.dasco.openhis.mapper"})
@EnableDubbo
public class DoctorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class,args);
        System.out.println("就诊子系统启动成功");
    }
}
