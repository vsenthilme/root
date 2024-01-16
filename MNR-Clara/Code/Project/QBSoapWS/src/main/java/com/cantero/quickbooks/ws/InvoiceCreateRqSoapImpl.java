package com.cantero.quickbooks.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.jws.WebService;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.qb.ws.services.WebApplicationContextLocator;
import com.mnrclara.qb.ws.services.model.AddInvoiceLine;
import com.mnrclara.qb.ws.services.model.ClientGeneral;
import com.mnrclara.qb.ws.services.model.InvoiceHeader;
import com.mnrclara.qb.ws.services.model.QBSync;
import com.mnrclara.qb.ws.services.utils.CommonUtils;

/*
 * http://developer.intuit.com/qbsdk-current/doc/pdf/qbwc_proguide.pdf
 */
@Component
@WebService(endpointInterface = "com.cantero.quickbooks.ws.QBWebConnectorSvcSoap")
public class InvoiceCreateRqSoapImpl implements QBWebConnectorSvcSoap {
	
	@Value("${mnrclara.soap.service}")
	private String mnrclaraSoapService;
	
	private String query = "";

	public InvoiceCreateRqSoapImpl() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    // alternative constructor to facilitate unit testing.
    protected InvoiceCreateRqSoapImpl(ApplicationContext context) {
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
    		System.out.println(jsonString);
    		sendResponse (jsonString);
		} catch (Exception e) {  
			System.out.println(e.toString());  
		}  
		
		return 100;
	}

	@Override
	public String sendRequestXML(String ticket, String strHCPResponse,
			String strCompanyFileName, String qbXMLCountry, int qbXMLMajorVers,
			int qbXMLMinorVers) {
		InvoiceHeader invoiceHeader = getInvoiceData();
		System.out.println ("invoiceHeader : " + invoiceHeader);
		if (invoiceHeader == null) { // No latest record is not existing
			query = "";
			return query;
		}
		
		// Getting Client General
		ClientGeneral clientGeneral = getClientData (invoiceHeader.getClientId());
		String custRef = invoiceHeader.getClientId() + ":" + invoiceHeader.getMatterNumber();
		System.out.println("custRef : " + custRef);
		String invoiceLines = "";
		if (invoiceHeader.getAddInvoiceLine() != null) {
			for (AddInvoiceLine invoiceLine : invoiceHeader.getAddInvoiceLine()) {
				String glDesc = getGLMappingDescription(invoiceLine.getItemNumber());
				if (invoiceLine.getBillableAmount() != null) {
					invoiceLines += "<InvoiceLineAdd>\n"
							+ "				<ItemRef>\n"
							+ "					<FullName>" + invoiceLine.getItemNumber() + "</FullName>\n"
							+ "				</ItemRef>\n"
							+ "				<Desc>" + glDesc + "</Desc>\n"
							+ "				<Quantity>1</Quantity>\n"
							+ "				<Rate> " + invoiceLine.getBillableAmount() + " </Rate>\n"
							+ "		</InvoiceLineAdd>";
				}
			}
		} else {
			System.out.println("There is no Invoice Lines in this Invoice.");
		}
		
		String inputDate = invoiceHeader.getInvoiceDate();
		inputDate = inputDate.substring(0, inputDate.indexOf("T"));
		System.out.println("invDate : " + inputDate);
		
		query = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+ "<?qbxml version=\"10.0\"?>\n"
				+ "<QBXML>\n"
				+ "  <QBXMLMsgsRq onError=\"stopOnError\">\n"
				+ "    <InvoiceAddRq requestID=\"" + invoiceHeader.getInvoiceNumber() + "\">\n"
				+ "      <InvoiceAdd>\n"
				+ "        <CustomerRef>\n"
				+ "          <FullName>" + custRef +  "</FullName>\n"
				+ "        </CustomerRef>\n"
				+ "        <TxnDate>" + inputDate + " </TxnDate>\n"
				+ "        <RefNumber>" + invoiceHeader.getInvoiceNumber() + " </RefNumber>\n"
				+ "        <BillAddress>\n"
				+ "          <Addr1>" + clientGeneral.getReferenceField16() +" </Addr1>\n"
				+ "          <City> " + clientGeneral.getReferenceField17() + " </City>\n"
				+ "          <State> " + clientGeneral.getReferenceField18() + " </State>\n"
				+ "          <PostalCode>" + clientGeneral.getReferenceField19() + "</PostalCode>\n"
				+ "          <Country> " + clientGeneral.getReferenceField20() + " </Country>\n"
				+ "        </BillAddress>\n"
				+ "        <PONumber></PONumber>\n"
				+ "        <Memo></Memo>\n"
				+ 			invoiceLines
				+ "      </InvoiceAdd>\n"
				+ "    </InvoiceAddRq>\n"
				+ "  </QBXMLMsgsRq>\n"
				+ "</QBXML>";
		return query;
	}
	
	/**
	 * 
	 * @return
	 */
	public InvoiceHeader getInvoiceData () {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(headers);
		String url = mnrclaraSoapService + "/invoice";
		ResponseEntity<InvoiceHeader> response = restTemplate.exchange(url, HttpMethod.GET, request, InvoiceHeader.class);
		return response.getBody();
	}
	
	/**
	 * 
	 * @return
	 */
	public ClientGeneral getClientData (String clientgeneralId) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(headers);
		// /soapservice/clientgeneral/{clientgeneralId}
		String url = mnrclaraSoapService + "/clientgeneral/" + clientgeneralId;
		ResponseEntity<ClientGeneral> response = 
				restTemplate.exchange(url, HttpMethod.GET, request, ClientGeneral.class);
		return response.getBody();
	}
	
	// GET GLMappingDescription
	public String getGLMappingDescription (Long itemNumber) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		// - /soapservice/glmapping/{itemNumber}
		String url = mnrclaraSoapService + "/glmapping/" + itemNumber;
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("languageId", "EN");

		ResponseEntity<String> response = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, String.class);
		System.out.println("GLMappingDescription Received from Clara: ---------" + response.getBody());
		return response.getBody();
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	public String sendResponse (String response) {
		InvoiceCreateRet invoiceRet = convertJSONtoObject (response);
		System.out.println("invoiceRet : " + invoiceRet);
		
		String url = mnrclaraSoapService + "/invoice/response";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		HttpEntity<?> entity = new HttpEntity<>(invoiceRet, headers);
		ResponseEntity<String> result = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
		return result.getBody();
	}
	
	/**
	 * 
	 * @param jsonstring
	 * @return
	 */
	private InvoiceCreateRet convertJSONtoObject (String jsonstring) {
		JSONObject mainObject = new JSONObject(jsonstring);
		JSONObject posts = mainObject.getJSONObject("QBXML");
		JSONObject posts1 = posts.getJSONObject("QBXMLMsgsRs");
		JSONObject posts2 = posts1.getJSONObject("InvoiceAddRs");
		try {
			String statusCode = posts2.get("statusCode").toString();
			System.out.println("statusCode :  " + statusCode);
			InvoiceCreateRet invoiceRet = new InvoiceCreateRet();
			String invoiceNumber = posts2.get("requestID").toString();
			System.out.println("invoiceNumber :  " + invoiceNumber);
			invoiceRet.setName(invoiceNumber);
			invoiceRet.setStatusCode(statusCode);
			if (!statusCode.equalsIgnoreCase("0")) {
				// Insert QBSync
				QBSync qbSync = new QBSync();
				qbSync.setId(invoiceRet.getName());
				qbSync.setObjectName("INVOICE");
				qbSync.setStatusId(2L);
				qbSync.setError(jsonstring);
				qbSync.setCreatedOn(new Date());
				createQbSync (qbSync);
			}
			
			return invoiceRet;
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
