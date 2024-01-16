package com.mnrclara.qb.ws.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class QbSoapWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(QbSoapWsApplication.class, args);
		System.out.println("Calling...");
//		Endpoint.publish("http://192.168.0.243:18081/ItemQueryRqSoapImpl",
//				new ItemQueryRqSoapImpl());
	}
}
