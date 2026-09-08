package com.example.billdemo;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.billdemo.mapper")
public class BillWebApplication {


    public static void main(String[] args) {

        SpringApplication.run(BillWebApplication.class,args);

    }

}
