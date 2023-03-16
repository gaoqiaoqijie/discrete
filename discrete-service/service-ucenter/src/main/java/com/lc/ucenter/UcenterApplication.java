package com.lc.ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ：lc
 * @date ：Created in 2021/2/21 16:21
 * @modified By：
 */
@SpringBootApplication()
@ComponentScan("com.lc")
@MapperScan("com.lc.ucenter.mapper")
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class);
    }
}
