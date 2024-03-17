package com.tekclover.wms.api.transaction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application-messages-axuat.properties")
//@PropertySource("classpath:application-messages.properties")
public class PropertiesConfig {

	@Value("${idmaster.oauth.credentials.client_id}")
	private String clientId;
	
	@Value("${idmaster.oauth.credentials.client_secret_key}")
	private String clientSecretKey;
	
	@Value("${idmaster.oauth.grant_type}")
	private String grantType;
	
	@Value("${idmaster.oauth.grant_type.username}")
	private String username;
	
	@Value("${idmaster.oauth.grant_type.password}")
	private String password;
	
	//-----------------------------------------------------------------------------------
	@Value("${transaction.oauth.access_token_url}")
	private String transactionAccessTokenUrl;
	
	@Value("${enterprise.oauth.access_token_url}")
	private String enterpriseAccessTokenUrl;
	
	@Value("${masters.oauth.access_token_url}")
	private String mastersAccessTokenUrl;
	
	@Value("${idmaster.oauth.access_token_url}")
	private String idmasterAccessTokenUrl;
	
	//-----------------------------------------------------------------------------------
	@Value("${api.transaction.service.url}")
	private String transactionServiceUrl;
	
	@Value("${api.enterprise.service.url}")
	private String enterpriseServiceUrl;
	
	@Value("${api.masters.service.url}")
	private String mastersServiceUrl;
	
	@Value("${api.idmaster.service.url}")
	private String idmasterServiceUrl;
	
	//------------------------AX-API-----------------------------------------------------
	// axapi.service.access_token.url=http://168.187.214.59:8040/api/ax/gettoken
	@Value("${axapi.service.access_token.url}")
	private String axapiAccessTokenUrl;
	
	// axapi.service.access_token.username=TestAxUser
	@Value("${axapi.service.access_token.username}")
	private String axapiAccessTokenUsername;
	
	// axapi.service.access_token.password=Wms-@tv@ndtsc-!nt00
	@Value("${axapi.service.access_token.password}")
	private String axapiAccessTokenPassword;
	
	//-----------------------------------------------------------------------------------
	// axapi.service.asn.url=http://168.187.214.59:8040/api/asn/productreceipt
	@Value("${axapi.service.asn.url}")
	private String axapiServiceAsnUrl;
	
	// axapi.service.storereturn.url=http://168.187.214.59:8040/api/transferorder/inreceipt
	@Value("${axapi.service.storereturn.url}")
	private String axapiServiceStoreReturnUrl;
	
	// axapi.service.soreturn.url=http://168.187.214.59:8040/api/salesorder/returnpackingslip
	@Value("${axapi.service.soreturn.url}")
	private String axapiServiceSOReturnUrl;
	
	// axapi.service.interwarehouse.url=http://168.187.214.59:8040/api/interwarehouse/orderreceipt
	@Value("${axapi.service.interwarehouse.url}")
	private String axapiServiceInterwareHouseUrl;
	
	//---------------------------------------------------------------------------------------
	// axapi.service.shipment.url=http://168.187.214.59:8040/api/transferorder/outshipment
	@Value("${axapi.service.shipment.url}")
	private String axapiServiceShipmentUrl;
	
	// axapi.service.returnpo.url=http://168.187.214.59:8040/api/return/poreceipt
	@Value("${axapi.service.returnpo.url}")
	private String axapiServiceReturnPOUrl;
	
	// axapi.service.iwhouseshipment.url=http://168.187.214.59:8040/api/Interwarehouse/ordershipment
	@Value("${axapi.service.iwhouseshipment.url}")
	private String axapiServiceIWHouseShipmentUrl;
	
	// axapi.service.saleorder.url=http://168.187.214.59:8040/api/salesorder/packingslip
	@Value("${axapi.service.salesorder.url}")
	private String axapiServiceSalesOrderUrl;
	
	//-------------INVENTORY-STRATEGY-CONF-----------------------------------------------------
	// #------SB_CTD_ON,SB_STBIN
	// order.allocation.strategy.choice=SB_CTD_ON
	@Value("${order.allocation.strategy.choice}")
	private String orderAllocationStrategyCoice;
	
	/*
	 * ---------------------AX-OAUTH-----------------------------------------------------------
	 * axapi.service.access_token.auth.url=https://login.microsoftonline.com/truevalue.com.kw/oauth2/token
	 * axapi.service.access_token.client_Id=a9ef22c7-c41f-4cc5-8d83-4eb222dd7734
	 * axapi.service.access_token.grant_type=client_credentials
	 * axapi.service.access_token.resource=https://tvd365-dev81cbb1e734974cffdevaos.axcloud.dynamics.com
	 * axapi.service.access_token.client_secret=Bx28Q~Sjz45kSetkvgBqZjt2g-fimhs~Cat5taVt
	 */
	@Value("${axapi.service.access_token.auth.url}")
	private String axapiServiceAccessTokenAuthUrl;
	
	@Value("${axapi.service.access_token.client_Id}")
	private String axapiServiceAccessTokenClientId;
	
	@Value("${axapi.service.access_token.grant_type}")
	private String axapiServiceAccessTokenGrantType;
	
	@Value("${axapi.service.access_token.resource}")
	private String axapiServiceAccessTokenResource;
	
	@Value("${axapi.service.access_token.client_secret}")
	private String axapiServiceAccessTokenClientSecret;
}
