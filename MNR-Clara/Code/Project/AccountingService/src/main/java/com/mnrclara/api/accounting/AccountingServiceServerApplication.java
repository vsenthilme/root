package com.mnrclara.api.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

//import com.tekclover.wms.core.config.JWTAuthorizationFilter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
@EnableAsync
@EnableResourceServer
@EnableAuthorizationServer
@EnableSwagger2
public class AccountingServiceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountingServiceServerApplication.class, args);
	}
}