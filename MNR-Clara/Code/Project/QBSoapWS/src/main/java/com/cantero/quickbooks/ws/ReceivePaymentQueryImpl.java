package com.cantero.quickbooks.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import com.google.gson.Gson;
import com.mnrclara.qb.ws.services.WebApplicationContextLocator;
import com.mnrclara.qb.ws.services.utils.CommonUtils;

import lombok.Data;

@Component
@WebService(endpointInterface = "com.cantero.quickbooks.ws.QBWebConnectorSvcSoap")
public class ReceivePaymentQueryImpl implements QBWebConnectorSvcSoap {
	
	@Value("${mnrclara.soap.service}")
	private String mnrclaraSoapService;
	
	private String query = "";

	public ReceivePaymentQueryImpl() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    // alternative constructor to facilitate unit testing.
    protected ReceivePaymentQueryImpl (ApplicationContext context) {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(new DefaultListableBeanFactory(context));
        bpp.processInjection(this);
    }

	@Override
	public ArrayOfString authenticate(String strUserName, String strPassword) {
		ArrayOfString arr = new ArrayOfString();
		arr.string = new ArrayList<String>();
		arr.string.add("The first element is a token for the web connector s session");
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
//			System.out.println("Response : " + response);
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
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1 = convertStringToDate ("2023-04-25");
//			date2 = convertStringToDate ("2023-05-12");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		LocalDate localStartDate = LocalDate.ofInstant(date1.toInstant(), ZoneId.systemDefault());
//		LocalDate localEndDate = LocalDate.ofInstant(date2.toInstant(), ZoneId.systemDefault());
//		LocalDateTime startDate = localStartDate.atTime(0, 0, 01);
//		LocalDateTime endDate = localEndDate.atTime(23, 59, 59);
		
		LocalDate currentDate = LocalDate.now();
		LocalDateTime startDate = currentDate.atTime(0, 0, 01);
		LocalDateTime endDate = currentDate.atTime(23, 59, 59);
		query = "<?xml version=\"1.0\" ?>\n"
				+ "<?qbxml version=\"13.0\"?>\n"
				+ "			<QBXML>\r\n"
				+ "		        <QBXMLMsgsRq onError=\"stopOnError\">\r\n"
				+ "	                <ReceivePaymentQueryRq>\r\n"
				+ "						<ModifiedDateRangeFilter> \r\n"
				+ "                     	<FromModifiedDate>" + startDate + "</FromModifiedDate>\r\n"
				+ "                         <ToModifiedDate>" +  endDate + "</ToModifiedDate> \r\n"
				+ "                     </ModifiedDateRangeFilter>\r\n"
				+ "                 </ReceivePaymentQueryRq>\r\n"
				+ "                </QBXMLMsgsRq>\r\n"
				+ "            </QBXML>";
		
		System.out.println("Constructed Payment Query : " + query);
		return query;
	}
	
	/**
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		return date;
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	public String sendResponse (String jsonString) {
		List<ReceivePaymentResponse> responseList = convertJSONtoObject (jsonString);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		HttpEntity<?> entity = new HttpEntity<>(responseList, headers);
		
		// /soapservice/invoiceQuery/response
		String url = mnrclaraSoapService + "/receivePaymentQuery/response";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		
		ResponseEntity<String> result = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
		return result.getBody();
	}
	
	/**
	 * 
	 * @param jsonstring
	 */
	private List<ReceivePaymentResponse> convertJSONtoObject (String jsonstring) {
		List<ReceivePaymentResponse> responseList = new ArrayList <>();
		JSONObject mainObject = new JSONObject(jsonstring);
		JSONObject posts = mainObject.getJSONObject("QBXML");
		JSONObject posts1 = posts.getJSONObject("QBXMLMsgsRs");
		JSONObject posts2 = posts1.getJSONObject("ReceivePaymentQueryRs");
		Integer statusCode = (Integer) posts2.get("statusCode");
		if (statusCode == 0) {
			try {
				JSONObject paymentobj = posts2.getJSONObject("ReceivePaymentRet");
				String customerRef = (String) paymentobj.getJSONObject("CustomerRef").get("FullName");
				String arAccountRef = (String) paymentobj.getJSONObject("ARAccountRef").get("ListID");
				String paymentMethodRef = (String) paymentobj.getJSONObject("PaymentMethodRef").get("FullName");
				
				ReceivePaymentRet receivePaymentRet = new Gson().fromJson(paymentobj.toString(), ReceivePaymentRet.class);
				ReceivePaymentResponse response = copyProperties (receivePaymentRet);
				response.setCustomerRef(customerRef);
				response.setARAccountRef(arAccountRef);
				response.setPaymentMethodRef(paymentMethodRef);
				responseList.add(response); 
			} catch (Exception e) {
				JSONArray paymentarr = posts2.getJSONArray("ReceivePaymentRet");
				for (int i=0; i<paymentarr.length(); i++) {
					JSONObject paymentobj = (JSONObject) paymentarr.get(i);
					ReceivePaymentRet receivePaymentRet = new Gson().fromJson(paymentarr.get(i).toString(), ReceivePaymentRet.class);
					ReceivePaymentResponse response = copyProperties (receivePaymentRet);
					String customerRef = null;
					String paymentMethodRef = null;
					try {
						customerRef = (String) paymentobj.getJSONObject("CustomerRef").get("FullName");
					} catch (Exception e2) {
						Integer iCustomerRef = (Integer) paymentobj.getJSONObject("CustomerRef").get("FullName");
						customerRef = String.valueOf(iCustomerRef);
					}
					
					String arAccountRef = (String) paymentobj.getJSONObject("ARAccountRef").get("ListID");
					
					try {
						if (paymentobj.getJSONObject("PaymentMethodRef") != null) {
							paymentMethodRef = (String) paymentobj.getJSONObject("PaymentMethodRef").get("FullName");
						}
					} catch (Exception e1) {
						paymentMethodRef = null;
					}
					
					response.setCustomerRef(customerRef);
					response.setARAccountRef(arAccountRef);
					response.setPaymentMethodRef(paymentMethodRef);
					responseList.add(response);
				}
			}
		}
		return responseList;
	}
	
	/**
	 * 
	 */
//	public static String getCurrentDate () {
//		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		LocalDateTime datetime = LocalDateTime.now();
//		String currentDatetime = datetime.format(newPattern);
//		return currentDatetime;
//	}
	
	/**
	 * 
	 * @param receivePaymentRet
	 * @return
	 */
	private ReceivePaymentResponse copyProperties (ReceivePaymentRet receivePaymentRet) {
		ReceivePaymentResponse response = new ReceivePaymentResponse();
		BeanUtils.copyProperties(receivePaymentRet, response, CommonUtils.getNullPropertyNames(receivePaymentRet));
		return response;
	}
	
	@Data
	class ReceivePaymentRet {
		private String TxnID;
		private String TimeCreated;
		private Long TxnNumber;
		private CustomerRef customerRef;
		private ARAccountRef arAccountRef;
		private PaymentMethodRef paymentMethodRef;
		private String ListID;
		private String TxnDate;
		private String RefNumber;
		private Double TotalAmount;
	}
	
	@Data class CustomerRef {
		private String ListID;
		private String FullName;
	}
	
	@Data class ARAccountRef {
		private String ListID;
		private String FullName;
	}
	
	@Data class PaymentMethodRef {
		private String ListID;
		private String FullName;
	}
	
	//--------------------------------------------------------------------------------
	@Data
	class ReceivePaymentResponse {
		private String TxnID;
		private String TimeCreated;
		private Long TxnNumber;
		private String CustomerRef;
		private String ARAccountRef;
		private String PaymentMethodRef;
		private String ListID;
		private String TxnDate;
		private String RefNumber;
		private Double TotalAmount;
	}
	
	public static void main(String[] args) {
		LocalDate currentDate = LocalDate.now();
		currentDate = currentDate.minusDays(1);
		LocalDateTime startDate = currentDate.atTime(0, 0, 01);
		LocalDateTime endDate = currentDate.atTime(23, 59, 59);
//		LocalDate fourtyDaysBackDatedDate = currentDate.minusDays(60);
		System.out.println(startDate + "," + endDate);
	}
}
