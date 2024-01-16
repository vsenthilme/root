package com.cantero.quickbooks.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.mnrclara.qb.ws.services.WebApplicationContextLocator;
import com.mnrclara.qb.ws.services.model.ClientGeneral;
import com.mnrclara.qb.ws.services.model.MatterGenAcc;
import com.mnrclara.qb.ws.services.model.QBSync;

/*
 * http://developer.intuit.com/qbsdk-current/doc/pdf/qbwc_proguide.pdf
 */
@Component
@WebService(endpointInterface = "com.cantero.quickbooks.ws.QBWebConnectorSvcSoap")
public class MatterCreateRqSoapImpl implements QBWebConnectorSvcSoap {
	
	@Value("${mnrclara.soap.service}")
	private String mnrclaraSoapService;
	
	private String query = "";

	public MatterCreateRqSoapImpl() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    // alternative constructor to facilitate unit testing.
    protected MatterCreateRqSoapImpl(ApplicationContext context) {
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
    		System.out.println("Response received from QB: " + jsonString);
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
		String NO_OP_QUERY = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+ "<?qbxml version=\"7.0\"?>\n"
				+ "<QBXML>\n"
				+ "	<QBXMLMsgsRq onError=\"stopOnError\">\n"
				+ "		<CustomerAddRq requestID=\"Q3VzdG9tZXJBZGR8MTExMTIxMjE=\">\n"
				+ "			<CustomerAdd/>\n"
				+ "		</CustomerAddRq>\n"
				+ "	</QBXMLMsgsRq>\n"
				+ "</QBXML>";
		
		MatterGenAcc matterGeneral = getMatterData();
		System.out.println ("matterGeneral : " + matterGeneral);
		if (matterGeneral != null && matterGeneral.getSentToQB() == 0L) {
			// Getting Client General
			ClientGeneral clientGeneral = getClientData (matterGeneral.getClientId());
			String name = "";
			if (clientGeneral != null) {
				name = clientGeneral.getFirstNameLastName();
				if (name.length() > 40) { // Checks for maxlength
					name = name.substring(0, 40);
				}
			}
			query = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
					+ "<?qbxml version=\"7.0\"?>\n"
					+ "<QBXML>\n"
					+ "	<QBXMLMsgsRq onError=\"stopOnError\">\n"
					+ "<CustomerAddRq requestID=\"" + matterGeneral.getMatterNumber() + "\">\n"
					+ "  <CustomerAdd>\n"
					+ "    <Name>" + matterGeneral.getMatterNumber() + "</Name>\n"
//					+ "		<CompanyName>" + name + "</CompanyName>\n"
					+ "    <ParentRef>\n"
					+ "      <FullName>" + matterGeneral.getClientId() + "</FullName>\n"
					+ "    </ParentRef>\n"
					+ "    <Phone>" + clientGeneral.getContactNumber() + "</Phone>\n"
					+ "    <Email>" + clientGeneral.getEmailId() + "</Email>\n"
		//				+ "    <JobTitle>" + matterGeneral.getMatterDescription() + "</JobTitle> \n"
					+ "  </CustomerAdd>\n"
					+ "</CustomerAddRq>"
					+ "	</QBXMLMsgsRq>\n"
					+ "</QBXML>";
			System.out.println("Constructed Matter Query : " + query);
			return query;
		}
		return NO_OP_QUERY;
	}
	
	/**
	 * 
	 * @return
	 */
	public MatterGenAcc getMatterData () {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		String url = mnrclaraSoapService + "/mattergeneral";
		ResponseEntity<MatterGenAcc> response = restTemplate.exchange(url, HttpMethod.GET, request, MatterGenAcc.class);
		System.out.println("Response from clara service ---------" + response.getBody());
		return response.getBody();
	}
	
	/**
	 * 
	 * @param clientgeneralId
	 * @return
	 */
	public ClientGeneral getClientData (String clientgeneralId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> request = new HttpEntity<String>(headers);
			
			// /soapservice/clientgeneral/{clientgeneralId}
			String url = mnrclaraSoapService + "/clientgeneral/" + clientgeneralId;
			ResponseEntity<ClientGeneral> response = 
					restTemplate.exchange(url, HttpMethod.GET, request, ClientGeneral.class);
			System.out.println("ClientGeneral Received from Clara: ---------" + response.getBody());
			return response.getBody();
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	public String sendResponse (String jsonResponse) {
		CustomerRet response = convertJSONtoObject (jsonResponse);
		String url = mnrclaraSoapService + "/mattergeneral/response";
		System.out.println("url : " + url);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("response", response);
		HttpEntity<?> entity = new HttpEntity<>(response, headers);
		ResponseEntity<String> result = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
		System.out.println("result : " + result);
		return result.getBody();
	}
	
	/**
	 * 
	 * @param jsonstring
	 * @return
	 */
	private CustomerRet convertJSONtoObject (String jsonstring) {
		JSONObject mainObject = new JSONObject(jsonstring);
		JSONObject posts = mainObject.getJSONObject("QBXML");
		JSONObject posts1 = posts.getJSONObject("QBXMLMsgsRs");
		JSONObject posts2 = posts1.getJSONObject("CustomerAddRs");
		try {
			String statusCode = posts2.get("statusCode").toString();
			System.out.println("statusCode :  " + statusCode);
			CustomerRet customerRet = new CustomerRet();
			String matterNumber = posts2.get("requestID").toString();
			System.out.println("matterNumber :  " + matterNumber);
			
			customerRet.setName(matterNumber);
			customerRet.setStatusCode(statusCode);
			if (!statusCode.equalsIgnoreCase("0") && !statusCode.equalsIgnoreCase("3100") ) {
				// Insert QBSync
				QBSync qbSync = new QBSync();
				qbSync.setId(matterNumber);
				qbSync.setObjectName("MATTER");
				qbSync.setStatusId(2L);
				qbSync.setError(jsonstring);
				qbSync.setCreatedOn(new Date());
				createQbSync (qbSync);
			}
			
			return customerRet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param qbSync
	 * @return
	 */
	private String createQbSync (QBSync qbSync) {
		String url = mnrclaraSoapService + "/qbsync";
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		HttpEntity<?> entity = new HttpEntity<>(qbSync, headers);
		ResponseEntity<String> result = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
		return result.getBody();
	}
}
