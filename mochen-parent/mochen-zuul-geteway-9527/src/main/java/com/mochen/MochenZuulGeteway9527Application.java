package com.mochen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MochenZuulGeteway9527Application {

    public static void main(String[] args) {
        SpringApplication.run(MochenZuulGeteway9527Application.class, args);
    }

}
