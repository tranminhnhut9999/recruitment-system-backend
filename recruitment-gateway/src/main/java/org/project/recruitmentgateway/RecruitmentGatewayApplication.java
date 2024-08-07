package org.project.recruitmentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RecruitmentGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruitmentGatewayApplication.class, args);
    }

}
