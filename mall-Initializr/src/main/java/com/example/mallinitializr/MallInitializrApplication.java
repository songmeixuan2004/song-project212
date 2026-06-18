package com.example.mallinitializr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// 关键！扫描你的所有代码
@ComponentScan(basePackages = "com.example.shop")
@MapperScan(basePackages = "com.example.shop.mapper")
public class MallInitializrApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallInitializrApplication.class, args);
    }
}