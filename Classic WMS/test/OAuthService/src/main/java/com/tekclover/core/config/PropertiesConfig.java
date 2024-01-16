package com.tekclover.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@PropertySource("classpath:wmscore.properties")
@PropertySource("classpath:application-messages.properties")
@ConfigurationProperties
public class PropertiesConfig {

	@Value("${user-id}")
	private String userId;
	
	@Value("${user-name}")
    private String userName;
	
	@Value("${user.changePwd.success-message}")
	private String changePwdSucessMsg;
}
