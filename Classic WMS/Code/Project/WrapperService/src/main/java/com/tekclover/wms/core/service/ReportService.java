package com.tekclover.wms.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.exception.BadRequestException;
import com.tekclover.wms.core.model.auth.AuthToken;
import com.tekclover.wms.core.model.transaction.InboundIntegrationHeader;
import com.tekclover.wms.core.model.transaction.ReceiptConfimationReport;
import com.tekclover.wms.core.model.transaction.ShipmentDeliveryReport;
import com.tekclover.wms.core.model.transaction.ShipmentDeliveryReport;
import com.tekclover.wms.core.model.transaction.ShipmentDeliverySummaryReport;
import com.tekclover.wms.core.model.transaction.ShipmentDispatchSummaryReport;
import com.tekclover.wms.core.repository.MongoTransactionRepository;
import com.tekclover.wms.core.util.DateUtils;

import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class ReportService {

	@Autowired
	MongoTransactionRepository mongoInboundRepository;
	
//	@Autowired
//	MongoOutboundRepository mongoOutboundRepository;
	
	@Autowired
	private MastersService mastersService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * 
	 * @param warehouseID
	 * @param statusId
	 * @param orderDate
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, Object> getOrderDetails (String warehouseID, Long statusId, String orderDate) throws ParseException {
		Date localDate = null;
		List<InboundIntegrationHeader> inboundOrders = null;
		if (orderDate != null) {
			try {
				Date date = DateUtils.convertStringToDate(orderDate);
				localDate = DateUtils.addTimeToDate(date);
				
				inboundOrders = mongoInboundRepository.findAllByWarehouseIDAndProcessedStatusIdAndOrderReceivedOn(
						warehouseID, statusId, localDate);
				log.info("inboundOrders : " + inboundOrders);
			} catch (Exception e) {
				throw new BadRequestException("Date format should be MM-dd-yyyy");
			}
		} else {
			inboundOrders = mongoInboundRepository.findAllByWarehouseIDAndProcessedStatusId(
					warehouseID, statusId);
			log.info("inboundOrders : " + inboundOrders);
		}
			
		long newOrders = inboundOrders.stream().filter(a -> a.getProcessedStatusId() == 0).count();
		long processedOrders = inboundOrders.stream().filter(a -> a.getProcessedStatusId() == 10).count();
		
		Map<String, Object> map = new HashMap <>();
		map.put("newOrders", newOrders);
		map.put("processedOrders", processedOrders);
		map.put("orders", inboundOrders);
		return map;
	}
	
	/**
	 * 
	 * @param reportFormat
	 * @return
	 */
	public String exportBom (String reportFormat) {
		try {
			AuthToken authTokenForMasterService = authTokenService.getMastersServiceAuthToken();
			
			com.tekclover.wms.core.model.masters.BomHeader[] bomHeader = 
					mastersService.getBomHeaders(authTokenForMasterService.getAccess_token());
			
			int i = 300;
			for (com.tekclover.wms.core.model.masters.BomHeader bom : bomHeader) {
				List<String> lines = new ArrayList<>();
				lines.add("One Apple 123" + (i+=1000));
				lines.add("One Apple 234" + (i+=1000));
				lines.add("One Apple 345" + (i+=1000));
				bom.setLines(lines);
			}
			
			File file = ResourceUtils.getFile("classpath:bom.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(Arrays.asList(bomHeader));
			
			Map<String, Object> params = new HashMap<>();
			params.put ("createdBy", "Muru");
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);
			
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, "bom.html");
			}
			
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, "bom.pdf");
			}
			return reportFormat;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return reportFormat;
	}

	/**
	 * ShipmentDelivery
	 * @param shipmentDeliveryList
	 * @param reportFormat
	 * @return
	 */
	public String exportShipmentDelivery(ShipmentDeliveryReport[] shipmentDeliveryList, String reportFormat) {
		try {
			String reportPath = propertiesConfig.getReportPath();
			String customerRef = shipmentDeliveryList[0].getCustomerRef();
			String fileName = "/shipmentDelivery_" + customerRef + "_" + DateUtils.getCurrentTimestamp();
			
			File file = ResourceUtils.getFile("classpath:shipment_delivery.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(Arrays.asList(shipmentDeliveryList));
			
			Map<String, Object> params = new HashMap<>();
			params.put ("createdBy", "IWExpress");
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);
			
			if (reportFormat.equalsIgnoreCase("html")) {
				fileName = reportPath + fileName + ".html";
				JasperExportManager.exportReportToHtmlFile(jasperPrint, fileName);
			}
			
			if (reportFormat.equalsIgnoreCase("pdf")) {
				fileName = reportPath + fileName + ".pdf";
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
			}
			log.info("Done..........");
			return fileName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return reportFormat;
	}

	/**
	 * 
	 * @param shipmentDeliverySummaryReport
	 * @param reportFormat
	 * @return
	 */
	public String exportShipmentDeliverySummary(ShipmentDeliverySummaryReport[] shipmentDeliverySummaryReport,
			String reportFormat) {
		try {
			String reportPath = propertiesConfig.getReportPath();
			String fileName = "/shipmentDeliverySummaryReport_" + DateUtils.getCurrentTimestamp();
			File file = ResourceUtils.getFile("classpath:shipment_delivery_summary.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource datasource = 
					new JRBeanCollectionDataSource(Arrays.asList(shipmentDeliverySummaryReport));
			
			Map<String, Object> params = new HashMap<>();
			params.put ("createdBy", "IWExpress");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);
			
			if (reportFormat.equalsIgnoreCase("html")) {
				fileName = reportPath + fileName + ".html";
				JasperExportManager.exportReportToHtmlFile(jasperPrint, fileName);
			}
			
			if (reportFormat.equalsIgnoreCase("pdf")) {
				fileName = reportPath + fileName + ".pdf";
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
			}
			log.info("Done..........");
			return fileName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return reportFormat;
	}

	/**
	 * 
	 * @param shipmentDispatchSummary
	 * @param reportFormat
	 * @return
	 */
	public String exportShipmentDispatchSummary(ShipmentDispatchSummaryReport shipmentDispatchSummary, String reportFormat) {
		try {
			String reportPath = propertiesConfig.getReportPath();
			String fileName = "/shipmentDispatchSummaryReport_" + DateUtils.getCurrentTimestamp();
			File file = ResourceUtils.getFile("classpath:shipment_dispatch_summary.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource datasource = 
					new JRBeanCollectionDataSource(Arrays.asList(shipmentDispatchSummary));
			
			Map<String, Object> params = new HashMap<>();
			params.put ("createdBy", "IWExpress");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);
			
			if (reportFormat.equalsIgnoreCase("html")) {
				fileName = reportPath + fileName + ".html";
				JasperExportManager.exportReportToHtmlFile(jasperPrint, fileName);
			}
			
			if (reportFormat.equalsIgnoreCase("pdf")) {
				fileName = reportPath + fileName + ".pdf";
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
			}
			log.info("Done..........");
			return fileName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return reportFormat;
	}

	/**
	 * 
	 * @param receiptConfimationReport
	 * @param reportFormat
	 * @return
	 */
	public String exportReceiptConfimationReport(ReceiptConfimationReport receiptConfimationReport, String reportFormat) {
		try {
			String reportPath = propertiesConfig.getReportPath();
			String fileName = "/shipmentDispatchSummaryReport_" + DateUtils.getCurrentTimestamp();
			File file = ResourceUtils.getFile("classpath:receiptConfimation.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource datasource = 
					new JRBeanCollectionDataSource(Arrays.asList(receiptConfimationReport));
			
			Map<String, Object> params = new HashMap<>();
			params.put ("createdBy", "IWExpress");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);
			
			if (reportFormat.equalsIgnoreCase("html")) {
				fileName = reportPath + fileName + ".html";
				JasperExportManager.exportReportToHtmlFile(jasperPrint, fileName);
			}
			
			if (reportFormat.equalsIgnoreCase("pdf")) {
				fileName = reportPath + fileName + ".pdf";
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
			}
			log.info("Done..........");
			return fileName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return reportFormat;
	}

	//PDF Generate
	public void export(HttpServletResponse response) throws IOException, DocumentException {

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontTitle.setSize(15);

		Font fontSubTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontSubTitle.setSize(12);

		Paragraph paragraph = new Paragraph("Delivery Report", fontTitle);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		Paragraph paragraph4 = new Paragraph("Report Summary", fontSubTitle);
		paragraph4.setAlignment(Paragraph.ALIGN_LEFT);

		Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
		fontParagraph.setSize(8);

		Paragraph paragraph2 = new Paragraph("Selection Date: ", fontParagraph);
		paragraph2.setAlignment(Paragraph.ALIGN_RIGHT);
		Paragraph paragraph3 = new Paragraph("Run Date: ", fontParagraph);
		paragraph3.setAlignment(Paragraph.ALIGN_RIGHT);

		// Creating an ImageData object
		String imageFile = "D:/logo.png";
		Image image = Image.getInstance(imageFile);

		image.scaleToFit(100,1000);

		image.setAbsolutePosition(30,780);
		document.add(image);
		document.add(paragraph2);
		document.add(paragraph3);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(11);
		table.setWidthPercentage(105f);
		table.setWidths(new float[] {2.5f, 3.0f, 1.5f, 1.5f, 2.5f, 2.0f, 2.0f, 2.0f, 2.0f, 2.5f, 2.5f});
		table.setSpacingBefore(10);

		PdfPTable table2 = new PdfPTable(8);
		table2.setWidthPercentage(100f);
		table2.setWidths(new float[] {5.5f, 3.0f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f,2.5f});
		table2.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);
		writeSummaryTableHeader(table2);

		PdfReader reader = new PdfReader("OpenPDFExample.pdf");
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("File.pdf"));
		stamper.setRotateContents(false);
		Phrase t = new Phrase("Total pages " + reader.getNumberOfPages(), new Font(Font.HELVETICA, 14));
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			float xt = reader.getPageSize(i).getWidth()-50;
			float yt = reader.getPageSize(i).getBottom(5);
			ColumnText.showTextAligned(
					stamper.getOverContent(i), Element.ALIGN_RIGHT,
					t, xt, yt, 0);
		}
		stamper.close();
		reader.close();

		document.add(table);


		document.newPage();
		document.add(image);
		document.add(paragraph2);
		document.add(paragraph3);
		document.add(paragraph);
		document.add(paragraph4);

		document.add(table2);


		document.close();
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setPaddingBottom(5);
		cell.setBorderWidthBottom(1);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setSize(8.5f);
		font.setStyle(Font.BOLD);

		cell.setPhrase(new Phrase("Expected\n" + "Delivery Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Delivery Date/\n" + "Time", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Branch Code", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Order Type", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("# S.O", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("# Lines\n" + "Ordered", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("# Lines\n" + "Shipped", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("# Lines\n" + "Picked", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("# Ordered\n" + "Qty", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Shipped\n" + "Qty", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("%\n" +"Shipped", font));
		table.addCell(cell);

	}
	//Summary Table
	private void writeSummaryTableHeader(PdfPTable table2) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(1);
//		cell.setPaddingBottom(5);
		cell.setBorderWidthTop(1);
		cell.setBorderWidthBottom(1);
		cell.setBorderWidthLeft(1);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setSize(8.5f);
		font.setStyle(Font.BOLD);

		cell.setPhrase(new Phrase("Location", font));
		table2.addCell(cell);

		cell.setPhrase(new Phrase("Type", font));
		table2.addCell(cell);

		cell.setPhrase(new Phrase("No of\n" + "Orders", font));
		table2.addCell(cell);

		cell.setPhrase(new Phrase("# SKUs\n" + "Shipped", font));
		table2.addCell(cell);

		cell.setPhrase(new Phrase("#Lines\n" + "Picked", font));
		table2.addCell(cell);

		cell.setPhrase(new Phrase("Order Qty", font));
		table2.addCell(cell);

		cell.setPhrase(new Phrase("Shipped Qty", font));
		table2.addCell(cell);

		cell.setPhrase(new Phrase("% Shipped", font));
		cell.setBorderWidthRight(1);
		table2.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
//		for (User user : listUsers) {
//			table.addCell(String.valueOf(user.getId()));
//			table.addCell(user.getEmail());
//			table.addCell(user.getFullName());
//			table.addCell(user.getRoles().toString());
//			table.addCell(String.valueOf(user.isEnabled()));
//		}
	}


}
