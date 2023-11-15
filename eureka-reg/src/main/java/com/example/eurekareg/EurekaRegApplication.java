package com.example.eurekareg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaRegApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaRegApplication.class, args);
    }

}
