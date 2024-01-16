package com.cantero.quickbooks.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.jws.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.cantero.quickbooks.ws.ReceivePaymentQueryImpl.ReceivePaymentResponse;
import com.cantero.quickbooks.ws.ReceivePaymentQueryImpl.ReceivePaymentRet;
import com.google.gson.Gson;
import com.mnrclara.qb.ws.services.WebApplicationContextLocator;
import com.mnrclara.qb.ws.services.utils.CommonUtils;

import lombok.Data;

@Component
@WebService(endpointInterface = "com.cantero.quickbooks.ws.QBWebConnectorSvcSoap")
public class InvoiceQueryRqSoapImpl implements QBWebConnectorSvcSoap {
	
	@Value("${mnrclara.soap.service}")
	private String mnrclaraSoapService;
	
	private String query = "";

	public InvoiceQueryRqSoapImpl() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    // alternative constructor to facilitate unit testing.
    protected InvoiceQueryRqSoapImpl (ApplicationContext context) {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(new DefaultListableBeanFactory(context));
        bpp.processInjection(this);
    }

	@Override
	public ArrayOfString authenticate(String strUserName, String strPassword) {
		ArrayOfString arr = new ArrayOfString();
		arr.string = new ArrayList<String>();
		arr.string.add("The first element is a token for the web connector�s session");
		arr.string.add(""); //To use the currently open company, specify an empty string
		return arr;
	}

	@Override
	public String closeConnection(String ticket) {
		return null;
	}

	@Override
	public String connectionError(String ticket, String hresult, String message) {
		return null;
	}

	@Override
	public String getLastError(String ticket) {
		return null;
	}

	/**
	 * @return A positive integer less than 100 represents the percentage of work completed. A value of 1 means one percent complete, a value of 100 means 100 percent complete--there is no more work. A negative value means an error has occurred and the Web Connector responds to this with a getLastError call. The negative value could be used as a custom error code.
	 */
	@Override
	public int receiveResponseXML(String ticket, String response, String hresult, String message) {
		try {  
			JSONObject json = XML.toJSONObject(response);  
    		String jsonString = json.toString(4);
    		System.out.println("Response : " + jsonString);
    		sendResponse (jsonString);
		} catch (JSONException e) {  
			System.out.println(e.toString());  
		}  
		return 100;
	}

	@Override
	public String sendRequestXML(String ticket, String strHCPResponse,
			String strCompanyFileName, String qbXMLCountry, int qbXMLMajorVers,
			int qbXMLMinorVers) {
		// Getting Invoice Query Parameter from Clara
		Long invoiceNumber = getInvoiceQueryNumber();
		query = "<?xml version=\"1.0\" ?>\n"
				+ "<?qbxml version=\"8.0\"?>\n"
				+ "	<QBXML>\n"
				+ "		<QBXMLMsgsRq onError=\"stopOnError\">\n"
				+ "			<InvoiceQueryRq>\n"
				+ "			<RefNumber>" + invoiceNumber + "</RefNumber> \n"
				+ "			</InvoiceQueryRq>\n"
				+ "		</QBXMLMsgsRq>\n"
				+ "	</QBXML>";
//		query = "<?xml version=\"1.0\" ?>\n"
//				+ "<?qbxml version=\"8.0\"?>\n"
//				+ "<QBXML>\n"
//				+ "  <QBXMLMsgsRq onError=\"stopOnError\">\n"
//				+ "  	<InvoiceQueryRq requestID=\"" + invoiceNumber + "\" >\n"
//				+ "			<RefNumber>" + invoiceNumber + "</RefNumber> \n"
//				+ "  </QBXMLMsgsRq>\n"
//				+ "</QBXML>";
		System.out.println("Constructed Invoice Query : " + query);
		return query;
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getInvoiceQueryNumber () {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		// /soapservice/invoiceQuery
		String url = mnrclaraSoapService + "/invoiceQuery";
		ResponseEntity<Long> response = 
				restTemplate.exchange(url, HttpMethod.GET, request, Long.class);
		System.out.println("InvoiceQuery Received from Clara: ---------" + response.getBody());
		return response.getBody();
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	public String sendResponse (String jsonString) {
		InvoiceRet response = convertJSONtoObject (jsonString);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		HttpEntity<?> entity = new HttpEntity<>(response, headers);
		
		// /soapservice/invoiceQuery/response
		String url = mnrclaraSoapService + "/invoiceQuery/response";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		
		ResponseEntity<String> result = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
		System.out.println("result : " + result.getBody());
		return result.getBody();
	}
	
	/**
	 * 
	 * @param jsonstring
	 */
	private InvoiceRet convertJSONtoObject (String jsonstring) {
		JSONObject mainObject = new JSONObject(jsonstring);
		JSONObject posts = mainObject.getJSONObject("QBXML");
		JSONObject posts1 = posts.getJSONObject("QBXMLMsgsRs");
		JSONObject posts2 = posts1.getJSONObject("InvoiceQueryRs");
		InvoiceRet invoiceRet = new InvoiceRet();
		try {
			JSONArray invoiceRetJsonArr = posts2.getJSONArray("InvoiceRet");
			System.out.println("InvoiceRet : " + invoiceRetJsonArr);
			for (int i = 0; i < invoiceRetJsonArr.length(); i++) {
				JSONObject invoiceobj = (JSONObject) invoiceRetJsonArr.get(i);
				invoiceRet = processResponse (invoiceobj);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			String statusMessage = posts2.get("statusMessage").toString();
			System.out.println("statusMessage-----> : " + statusMessage);
			
			String statusCode = posts2.get("statusCode").toString();
			System.out.println("statusCode-----> : " + statusCode);
			if (statusCode.equalsIgnoreCase("0")){
				JSONObject invoiceobj = posts2.getJSONObject("InvoiceRet");
				invoiceRet = processResponse (invoiceobj);
			}
			
			if (statusMessage.indexOf("\"") > 0) {
				String i = statusMessage.substring(statusMessage.indexOf("\"") + 1);
				System.out.println("statusMessage----i----> : " + i);
				
				String invoiceNumber = i.substring(0,i.indexOf("\""));
				System.out.println("invoiceNumber :  " + invoiceNumber);
				
				invoiceRet.setRefNumber(Long.valueOf(invoiceNumber));
				invoiceRet.setError("500"); // Error Status Code
			}
			
			return invoiceRet;
		}
		return invoiceRet;
	}
	
	/**
	 * 
	 * @param invoiceobj
	 * @return
	 */
	private InvoiceRet processResponse (JSONObject invoiceobj) {
		InvoiceRet invoiceRetResponse = new Gson().fromJson(invoiceobj.toString(), InvoiceRet.class);
		InvoiceRet response = copyProperties (invoiceRetResponse);
		
		try {
			String refNumber = invoiceobj.get("RefNumber").toString();
			String txnID = invoiceobj.get("TxnID").toString();
			String timeCreated = invoiceobj.get("TimeCreated").toString();
			String timeModified = invoiceobj.get("TimeModified").toString();
			System.out.println("timeModified : " + timeModified);
			
			String appliedAmount = invoiceobj.get("AppliedAmount").toString();
			String isPaid = invoiceobj.get("IsPaid").toString();
			String balanceRemaining = invoiceobj.get("BalanceRemaining").toString();
			
			response.setRefNumber(Long.valueOf(refNumber));
			response.setTxnID(txnID);
			response.setTimeCreated(timeCreated);
			response.setTimeModified(timeModified);
			
			if (appliedAmount == null) {
				response.setAppliedAmount(0D);
			} else {
				response.setAppliedAmount(Double.valueOf(appliedAmount));
			}
			
			if (isPaid.equalsIgnoreCase("false")) {
				response.setIsPaid(Boolean.FALSE);
			} else {
				response.setIsPaid(Boolean.TRUE);
			}
			
			if (balanceRemaining == null) {
				response.setBalanceRemaining(0D);
			} else {
				response.setBalanceRemaining(Double.valueOf(balanceRemaining));
			}
			response.setError("0"); // Success Status Code
			return response;
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 
	 * @param invoiceRetResponse
	 * @return
	 */
	private InvoiceRet copyProperties (InvoiceRet invoiceRetResponse) {
		InvoiceRet response = new InvoiceRet();
		BeanUtils.copyProperties(invoiceRetResponse, response, CommonUtils.getNullPropertyNames(invoiceRetResponse));
		return response;
	}
	
	@Data
	class InvoiceRet {
		private Double BalanceRemaining;
		private Long RefNumber;
		private Double Subtotal;
		private Double AppliedAmount;
		private String ShipDate;
		private Boolean IsFinanceCharge;
		private Boolean IsToBeEmailed;
		private String TimeCreated;
		private String TimeModified;
		private String TxnID;
		private Double SalesTaxTotal;
		private Boolean IsPaid;
		private Boolean IsToBePrinted;
		private String error;
	}
}
