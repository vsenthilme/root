package com.flourish.b2b.api.gateway.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flourish.b2b.api.gateway.config.PropertiesConfig;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.ErrorOrder;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.NewConsignmentOutbound;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.NewOrder;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.Order;
import com.flourish.b2b.api.gateway.repository.ConsignmentOutboundErrorLogRepository;
import com.flourish.b2b.api.gateway.repository.ConsignmentOutboundRepository;
import com.flourish.b2b.api.gateway.util.CopyBeanProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsignmentOutboundService {

	@Autowired
	private CommonService commonService;

	@Autowired
	private ConsignmentOutboundRepository conOutboundRepository;

	@Autowired
	private ConsignmentOutboundErrorLogRepository conOutboundErrorLogRepository;

	@Autowired
	PropertiesConfig propertiesConfig;

//	final private String simulatorUrl = "http://localhost:8085/v1/simulator/consignment-outbound";

	/**
	 * getSimulatorApiUrl ()
	 * 
	 * @return
	 */
	private String getSimulatorApiUrl() {
		return propertiesConfig.getSpSimulatorUrl();
	}

	/**
	 * getConsignments
	 * 
	 * @return
	 */
	public List<Order> getConsignments() {
		return conOutboundRepository.findAll();
	}

	public Order getConsignmentByShipmentOrderNo(String shipmentOrderNo) {
		return conOutboundRepository.findByShipmentOrderNo(shipmentOrderNo);
	}

	/**
	 * postConsignment
	 * 
	 * @param newOrder
	 * @return
	 * @throws JsonProcessingException
	 */
	public String postConsignment(NewOrder newOrder) throws JsonProcessingException {
		Order dbOrder = new Order();
		Order createdOrder = null;
		try {
			dbOrder = CopyBeanProperties.copyProperties(dbOrder, newOrder);
			createdOrder = conOutboundRepository.save(dbOrder);
			log.info("createdOrder:::: " + createdOrder);
		} catch (DataIntegrityViolationException e) {
			log.error("DataIntegrityViolationException ERROR::: " + e.getRootCause().getLocalizedMessage());
			String jsonString = convertObjectToJson(dbOrder);
			saveErrorOrder(jsonString, e.getRootCause().getLocalizedMessage());
			return e.getRootCause().getLocalizedMessage();
		} catch (Exception e) {
			log.error("Exception Occured::: " + e.getLocalizedMessage());
			String jsonString = convertObjectToJson(dbOrder);
			saveErrorOrder(jsonString, e.getLocalizedMessage());
			return e.getLocalizedMessage();
		}

		/*
		 * Push Orders to Service Provider
		 */
		if (createdOrder != null) {
			NewConsignmentOutbound newConsignmentOutbound = new NewConsignmentOutbound();
			try {
				BeanUtils.copyProperties(newConsignmentOutbound, createdOrder);
				String response = postOrderToServiceProvider(newConsignmentOutbound);
				log.info(response);
				return response;
			} catch (Exception e) {
				return "Error on post Data to Service Provider" + e.getMessage();
			}
		}
		return null;
	}

	/**
	 * postOrderToServiceProvider
	 * 
	 * @return
	 */
	public String postOrderToServiceProvider(NewConsignmentOutbound newConsignmentOutbound) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String authToken = commonService.generateOAuthToken();

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Spring's RestTemplate"); // value can be whatever
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(newConsignmentOutbound, headers);
			String result = restTemplate.postForObject(getSimulatorApiUrl(), entity, String.class);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * ErrorLog Repo Services
	 */

	/**
	 * saveErrorOrder
	 * 
	 * @param sourceJsonString
	 * @param errorMsg
	 * @return
	 */
	private boolean saveErrorOrder(String sourceJsonString, String errorMsg) {
		ErrorOrder errorOrder = new ErrorOrder();
		errorOrder.setSourceJson(sourceJsonString);
		errorOrder.setRemarks(errorMsg);
		ErrorOrder createdErrorOrder = conOutboundErrorLogRepository.save(errorOrder);
		if (createdErrorOrder != null) {
			return true;
		}
		return false;
	}

	/**
	 * convertObjectToJson
	 * 
	 * @param object
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertObjectToJson(Object object) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(object);
		return json;
	}

	/**
	 * getConsignmentOutboundErrorLogs
	 * 
	 * @return
	 */
	public List<ErrorOrder> getConsignmentOutboundErrorLogs() {
		return conOutboundErrorLogRepository.findAll();
	}

	/**
	 * getConsignmentOutboundErrorLog
	 * 
	 * @param id
	 * @return
	 */
	public ErrorOrder getConsignmentOutboundErrorLog(Long id) {
		return conOutboundErrorLogRepository.findByErrorId(id);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public List<NewOrder> readExcelFile(MultipartFile file) {
		try {
			File convFile = multipartToFile(file, file.getOriginalFilename());
			FileInputStream excelFile = new FileInputStream(convFile);

			Workbook workbook = new XSSFWorkbook(excelFile);

			Sheet sheet = workbook.getSheet("NewOrder");
			Iterator<Row> rows = sheet.iterator();

			List<NewOrder> lstNewOrders = new ArrayList<NewOrder>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				NewOrder newOrder = new NewOrder();

				int cellIndex = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					if (cellIndex == 0) {
						newOrder.setShipperCode(String.valueOf(currentCell.getNumericCellValue()));
					} else if (cellIndex == 1) {
						newOrder.setShipmentOrdertype(currentCell.getStringCellValue());
					} else if (cellIndex == 2) {
						newOrder.setShipmentOrderNo(currentCell.getStringCellValue());
					} else if (cellIndex == 3) {
						newOrder.setDeliveryType(currentCell.getStringCellValue());
					} else if (cellIndex == 4) {
						newOrder.setStatus(currentCell.getStringCellValue());
					} else if (cellIndex == 5) {
						newOrder.setConsignementType(currentCell.getStringCellValue());
					} 
					cellIndex++;
				}
				lstNewOrders.add(newOrder);
			}
			return lstNewOrders;
		} catch (IOException e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}
	
	/**
	 * multipartToFile
	 * @param multipart
	 * @param fileName
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private File multipartToFile(MultipartFile multipart, String fileName)
			throws IllegalStateException, IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
		multipart.transferTo(convFile);
		return convFile;
	}
}
