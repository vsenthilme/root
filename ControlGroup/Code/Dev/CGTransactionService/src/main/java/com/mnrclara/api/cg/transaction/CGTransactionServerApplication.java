package com.mnrclara.api.cg.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableResourceServer
@EnableAuthorizationServer
@EnableSwagger2
public class CGTransactionServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CGTransactionServerApplication.class, args);
    }
}