package com.tekclover.wms.api.idmaster.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {

//--------------EMAIL-------------------------------------------------------------

	@Value("${email.from.address}")
	private String emailFromAddress;
	
}
