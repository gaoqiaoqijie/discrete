package com.lc.qa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ：lc
 * @date ：Created in 2021/4/2 18:28
 * @modified By：
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan("com.lc")
@MapperScan("com.lc.qa.mapper")
public class QaApplication {
    public static void main(String[] args) {
        SpringApplication.run(QaApplication.class, args);
    }
}
