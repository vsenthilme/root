package com.mnrclara.api.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

//import com.tekclover.wms.core.config.JWTAuthorizationFilter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableResourceServer
@EnableAuthorizationServer
@EnableSwagger2
@EnableScheduling
@EnableMongoRepositories
public class CRMServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CRMServerApplication.class, args);
	}
}