package com.iweb2b.api.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {

	@Value("${master.oauth.credentials.client_id}")
	private String clientId;
	
	@Value("${master.oauth.credentials.client_secret_key}")
	private String clientSecretKey;
	
	@Value("${master.oauth.grant_type}")
	private String grantType;
	
	@Value("${master.oauth.grant_type.username}")
	private String username;
	
	@Value("${master.oauth.grant_type.password}")
	private String password;
	
	//---------------------Shipsy APIs---------------------------------------------------
	// shipsy.api.server=https://demodashboardapi.shipsy.in
	@Value("${shipsy.api.server}")
	private String shipsyApiServer;
	
	// shipsy.api.auth.token
	@Value("${shipsy.api.auth.token}")
	private String shipsyApiAuthtoken;
	
	//------------------Softdata upload--------------------------------------------------
	// shipsy.api.softdata.upload=/api/client/integration/consignment/upload/softdata/v2
	@Value("${shipsy.api.softdata.upload}")
	private String shipsyApiSoftdataUpload;
	
	// shipsy.api.softdata.update=/api/client/integration/consignment/event
	@Value("${shipsy.api.softdata.update}")
	private String shipsyApiSoftdataUpdate;
	
	//------------------Consignment Tracking---------------------------------------------
	// shipsy.api.consignment.tracking=/api/client/integration/consignment/track
	@Value("${shipsy.api.consignment.tracking}")
	private String shipsyApiConsignmentTracking;
	
	// shipsy.api.order.cancel=/api/client/integration/consignment/cancellation
	@Value("${shipsy.api.order.cancel}")
	private String shipsyApiOrderCancel;
	
	//------------------Client Label Generation Upload-----------------------------------
	// shipsy.api.labelgen=/api/client/integration/consignment/shippinglabel/stream
	@Value("${shipsy.api.labelgen}")
	private String shipsyApiLabelGen;
	
	//------------------Webhook----------------------------------------------------
	// shipsy.api.asyad.webhook=https://apix.asyadexpress.com/v2/webhooks/incoming-integration
	@Value("${shipsy.api.asyad.webhook}")
	private String shipsyApiAsyadWebhook;
	
	//==============================================================================
	//-------------------JNT--------------------------------------------------------
	// jnt.customerCode=J0086003429
	@Value("${jnt.customerCode}")
	private String jntCustomerCode;
	
	// jnt.address=https://demoopenapi.jtjms-sa.com/webopenplatformapi/api/order/addOrder
	@Value("${jnt.address}")
	private String jntAddress;
	
	// jnt.printLabel.address=https://openapi.jtjms-sa.com/webopenplatformapi/api/order/printOrderUrl
	@Value("${jnt.printLabel.address}")
	private String jntPrintLabelAddress;
	
	// jnt.printLabel.address=https://openapi.jtjms-sa.com/webopenplatformapi/api/order/printOrder
	@Value("${jnt.printLabel.pdf.address}")
	private String jntPdfPrintLabelAddress;
	
	// jnt.apiAccount=292508153084379141
	@Value("${jnt.apiAccount}")
	private String jntApiAccount;
	
	// jnt.privateKey=a0a1047cce70493c9d5d29704f05d0d9
	@Value("${jnt.privateKey}")
	private String jntPrivateKey;
	
	// jnt.bz.signature=VdlpKaoq64AZ0yEsBkvt1A==
	@Value("${jnt.bz.signature}")
	private String jntBzSignature;
	
	//=================================================================================
	//-------------------QATAR-APIs----------------------------------------------------
	/*
	 * qp.address=https://web.qatarpost.qa/shipmentapi
	 * qp.token=/get/token
	 * qp.token.username=IwEx
	 * qp.token.password=fV87Br
	 * qp.partnerToken=/partner/authentication/get/authcode/
	 * qp.create=/partner/order/create
	 * qp.tracking=/partner/order/trackingNumbers
	 */
	@Value("${qp.address}")
	private String qpAddress;
	
	@Value("${qp.token}")
	private String qpToken;
	
	@Value("${qp.token.username}")
	private String qpTokenUsername;
	
	@Value("${qp.token.password}")
	private String qpTokenPassword;

	@Value("${qp.partnerToken}")
	private String qpPartnerToken;

	@Value("${qp.create}")
	private String qpCreate;

	@Value("${qp.tracking}")
	private String qpTracking;
}
