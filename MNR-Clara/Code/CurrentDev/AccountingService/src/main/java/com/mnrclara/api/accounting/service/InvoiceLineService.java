package com.mnrclara.api.accounting.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.accounting.controller.exception.BadRequestException;
import com.mnrclara.api.accounting.model.invoice.AddInvoiceLine;
import com.mnrclara.api.accounting.model.invoice.InvoiceLine;
import com.mnrclara.api.accounting.model.invoice.UpdateInvoiceLine;
import com.mnrclara.api.accounting.repository.InvoiceLineRepository;
import com.mnrclara.api.accounting.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvoiceLineService {
	
	@Autowired
	private InvoiceLineRepository invoiceLineRepository;
	
	/**
	 * getInvoiceLines
	 * @return
	 */
	public List<InvoiceLine> getInvoiceLines () {
		List<InvoiceLine> invoiceLineList =  invoiceLineRepository.findAll();
		invoiceLineList = invoiceLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return invoiceLineList;
	}
	
	/**
	 * getInvoiceLine
	 * @param invoiceFiscalYear
	 * @return
	 */
	public List<InvoiceLine> getInvoiceLine (String invoiceNumber) {
		List<InvoiceLine> invoiceLine = invoiceLineRepository.findByInvoiceNumber(invoiceNumber);
		if (invoiceLine == null) {
			throw new BadRequestException("Error on getting InvoiceLine : " 
						+ invoiceNumber + " due to record does not exist.");
		}
		return invoiceLine;
	}
	
	/**
	 * 
	 * @param invoiceNumber
	 * @param itemNumber
	 * @return
	 */
	public InvoiceLine getInvoiceLine (String invoiceNumber, Long itemNumber) {
		InvoiceLine invoiceLine = invoiceLineRepository.findByInvoiceNumberAndItemNumber(invoiceNumber, itemNumber);
		if (invoiceLine == null) {
			throw new BadRequestException("Error on getting InvoiceLine : " 
						+ invoiceNumber + " due to record does not exist.");
		}
		return invoiceLine;
	}
	
	/**
	 * createInvoiceLine
	 * @param newInvoiceLine
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public InvoiceLine createInvoiceLine (AddInvoiceLine newInvoiceLine, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<InvoiceLine> invoiceline = 
				invoiceLineRepository.findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndInvoiceNumberAndInvoiceFiscalYearAndInvoicePeriodAndItemNumberAndDeletionIndicator(
						newInvoiceLine.getLanguageId(),
						newInvoiceLine.getClassId(),
						newInvoiceLine.getMatterNumber(),
						newInvoiceLine.getClientId(),
						newInvoiceLine.getInvoiceNumber(),
						newInvoiceLine.getInvoiceFiscalYear(),
						newInvoiceLine.getInvoicePeriod(),
						newInvoiceLine.getItemNumber(),
						0L);
		if (!invoiceline.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		InvoiceLine dbInvoiceLine = new InvoiceLine();
		log.info("newInvoiceLine : " + newInvoiceLine);
		BeanUtils.copyProperties(newInvoiceLine, dbInvoiceLine);
		dbInvoiceLine.setDeletionIndicator(0L);
		dbInvoiceLine.setCreatedBy(loginUserID);
		dbInvoiceLine.setUpdatedBy(loginUserID);
		dbInvoiceLine.setCreatedOn(new Date());
		dbInvoiceLine.setUpdatedOn(new Date());
		return invoiceLineRepository.save(dbInvoiceLine);
	}
	
	/**
	 * updateInvoiceLine
	 * @param loginUserId 
	 * @param invoiceFiscalYear
	 * @param long1 
	 * @param updateInvoiceLine
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public InvoiceLine updateInvoiceLine (String invoiceNumber, Long itemNumber, UpdateInvoiceLine updateInvoiceLine, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		InvoiceLine dbInvoiceLine = getInvoiceLine(invoiceNumber, itemNumber);
		BeanUtils.copyProperties(updateInvoiceLine, dbInvoiceLine, CommonUtils.getNullPropertyNames(updateInvoiceLine));
		dbInvoiceLine.setUpdatedBy(loginUserID);
		dbInvoiceLine.setUpdatedOn(new Date());
		return invoiceLineRepository.save(dbInvoiceLine);
	}
	
	/**
	 * deleteInvoiceLine
	 * @param loginUserID 
	 * @param invoiceFiscalYear
	 */
	public void deleteInvoiceLine (String invoiceNumber, String loginUserID) {
		List<InvoiceLine> invoiceLines = getInvoiceLine(invoiceNumber);
		if ( invoiceLines.isEmpty() ) {
			throw new EntityNotFoundException("Error in deleting Id: " + invoiceNumber);
		}
		
		for (InvoiceLine invoiceLine : invoiceLines) {
			invoiceLine.setDeletionIndicator(1L);
			invoiceLine.setUpdatedBy(loginUserID);
			invoiceLine = invoiceLineRepository.save(invoiceLine);
			log.info("InvoiceLine deleted : " + invoiceLine);
		}
	}
}
