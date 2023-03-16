package com.lc.home;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ：lc
 * @date ：Created in 2021/3/2 21:33
 * @modified By：
 */
@SpringBootApplication
@ComponentScan("com.lc")
@MapperScan("com.lc.home.mapper")
public class HomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeApplication.class, args);
    }
}
