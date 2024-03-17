package com.mnrclara.wrapper.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableSwagger2
public class MNRClaraWrapperServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MNRClaraWrapperServiceApplication.class, args);
	}
}
