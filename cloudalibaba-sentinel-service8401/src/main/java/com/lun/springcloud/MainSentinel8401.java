package com.lun.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MainSentinel8401 {
    public static void main(String[] args) {
        SpringApplication.run(MainSentinel8401.class,args);
    }
}
