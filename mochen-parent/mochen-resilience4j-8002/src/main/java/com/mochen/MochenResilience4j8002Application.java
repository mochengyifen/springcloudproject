package com.mochen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableEurekaClient
@EnableFeignClients(basePackages= {"com.mochen"})
public class MochenResilience4j8002Application {

    public static void main(String[] args) {
        SpringApplication.run(MochenResilience4j8002Application.class, args);
    }

}
