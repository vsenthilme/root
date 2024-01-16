package com.mnrclara.qb.ws.services.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cantero.quickbooks.ws.ClientCreateRqSoapImpl;

@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/qbws")
@RestController
public class QBController {

//	@Autowired
	ClientCreateRqSoapImpl clientCreateRqSoap;

	@GetMapping("/postdata")
	public ResponseEntity<?> postActivityCode() throws Exception {
		String query = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+ "<?qbxml version=\"7.0\"?>\n"
				+ "<QBXML>\n"
				+ "	<QBXMLMsgsRq onError=\"stopOnError\">\n"
				+ "\n"
				+ "		<CustomerAddRq requestID=\"Q3VzdG9tZXJBZGR8MTExMTIxMjE=\">\n"
				+ "			<CustomerAdd>\n"
				+ "				<Name>Karthik</Name>\n"
				+ "				<FirstName>Karthik</FirstName>\n"
				+ "				<MiddleName>D</MiddleName>\n"
				+ "				<LastName>D</LastName>\n"
				+ "				<BillAddress>\n"
				+ "					<Addr1>Thiruvalluvar street</Addr1>\n"
				+ "					<City>WestMamabalam</City>\n"
				+ "					<State>TN</State>\n"
				+ "					<PostalCode>600221</PostalCode>\n"
				+ "					<Country>INDIA</Country>\n"
				+ "				</BillAddress>\n"
				+ "				<Phone>444-3333-4343</Phone>\n"
				+ "				<Email>karthik@example.com</Email>\n"
				+ "				<Contact>Karthik</Contact>\n"
				+ "			</CustomerAdd>\n"
				+ "		</CustomerAddRq>\n"
				+ "	</QBXMLMsgsRq>\n"
				+ "</QBXML>";
		return new ResponseEntity<>("Added", HttpStatus.OK);
	}
	
	@GetMapping("")
	public ResponseEntity<?> getSoapCall() throws Exception {
		return new ResponseEntity<>("SOAPWS", HttpStatus.OK);
	}
}