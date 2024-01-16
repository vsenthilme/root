package com.mnrclara.qb.ws.services;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.ws.Endpoint;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cantero.quickbooks.ws.ClientCreateRqSoapImpl;
import com.cantero.quickbooks.ws.InvoiceCreateRqSoapImpl;
import com.cantero.quickbooks.ws.InvoiceQueryRqSoapImpl;
import com.cantero.quickbooks.ws.MatterCreateRqSoapImpl;
import com.cantero.quickbooks.ws.ReceivePaymentQueryImpl;

@Configuration
public class WebApplicationContextLocator implements ServletContextInitializer {

    private static WebApplicationContext webApplicationContext;

    public static WebApplicationContext getCurrentWebApplicationContext() {
        return webApplicationContext;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		Endpoint.publish("http://localhost:9999/ClientCreateRqSoapImpl", new ClientCreateRqSoapImpl());
//		Endpoint.publish("http://localhost:9998/MatterCreateRqSoapImpl", new MatterCreateRqSoapImpl());
//		Endpoint.publish("http://localhost:9997/InvoiceCreateRqSoapImpl", new InvoiceCreateRqSoapImpl());
//		Endpoint.publish("http://localhost:9996/InvoiceQueryRqSoapImpl", new InvoiceQueryRqSoapImpl());
		
		Endpoint.publish("http://10.0.85.21:9999/ClientCreateRqSoapImpl", new ClientCreateRqSoapImpl());
		Endpoint.publish("http://10.0.85.21:9998/MatterCreateRqSoapImpl", new MatterCreateRqSoapImpl());
		Endpoint.publish("http://10.0.85.21:9997/InvoiceCreateRqSoapImpl", new InvoiceCreateRqSoapImpl());
		Endpoint.publish("http://10.0.85.21:9996/InvoiceQueryRqSoapImpl", new InvoiceQueryRqSoapImpl());
		Endpoint.publish("http://10.0.85.21:9995/ReceivePaymentQueryImpl", new ReceivePaymentQueryImpl());
    }
}
