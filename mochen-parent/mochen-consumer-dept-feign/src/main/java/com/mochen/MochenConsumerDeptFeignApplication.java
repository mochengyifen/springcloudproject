package com.mochen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages= {"com.mochen"})
public class MochenConsumerDeptFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(MochenConsumerDeptFeignApplication.class, args);
    }

}
