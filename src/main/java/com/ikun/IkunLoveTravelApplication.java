package com.ikun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.ikun.mapper")
public class IkunLoveTravelApplication {
    public static void main(String[] args) {
        SpringApplication.run(IkunLoveTravelApplication.class, args);
    }
}
