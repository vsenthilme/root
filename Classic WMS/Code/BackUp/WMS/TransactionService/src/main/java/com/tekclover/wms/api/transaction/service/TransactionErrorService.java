package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.tekclover.wms.api.transaction.model.dto.FindTransactionError;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.SearchInboundLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.GrLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.SearchGrLine;
import com.tekclover.wms.api.transaction.model.inbound.staging.SearchStagingHeader;
import com.tekclover.wms.api.transaction.model.inbound.staging.StagingHeader;
import com.tekclover.wms.api.transaction.model.report.SearchTransactionError;
import com.tekclover.wms.api.transaction.repository.specification.*;
import com.tekclover.wms.api.transaction.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.model.dto.TransactionError;
import com.tekclover.wms.api.transaction.repository.TransactionErrorRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionErrorService {
	
	@Autowired
	private TransactionErrorRepository transactionErrorRepository;
	
	public List<TransactionError> getTransactionErrors () {
		return transactionErrorRepository.findAll();
	}

	/**
	 *
	 * @param tableName
	 * @param transaction
	 * @param errorType
	 * @param errorDesc
	 * @param objectData
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public TransactionError createTransactionError (String tableName, String transaction, String errorType, String errorDesc, String objectData, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		TransactionError dbTransactionError = new TransactionError();
		dbTransactionError.setErrorId(System.currentTimeMillis());
		dbTransactionError.setTableName(tableName);
		dbTransactionError.setTransaction(transaction);	// What is the transaction
		dbTransactionError.setErrorType(errorType); 	// Whether Lock or something
		dbTransactionError.setErrorDesc(errorDesc);
		dbTransactionError.setObjectData(objectData);
		
		dbTransactionError.setCreatedBy(loginUserID);
		dbTransactionError.setCreatedOn(new Date());
		log.info("dbTransactionError----------> : " + dbTransactionError);
		return transactionErrorRepository.save(dbTransactionError);
	}

	//get all
	public Stream<TransactionError> findTransactionError()
			throws Exception {

		TransactionErrorSpecification spec = new TransactionErrorSpecification();
		Stream<TransactionError> results = transactionErrorRepository.stream(spec, TransactionError.class);
		return results;
	}

	/**
	 *
	 * @param findTransactionError
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<TransactionError> findTransactionError (FindTransactionError findTransactionError) throws Exception {
		if (findTransactionError.getStartCreatedOn() != null && findTransactionError.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findTransactionError.getStartCreatedOn(),
					findTransactionError.getEndCreatedOn());
			findTransactionError.setStartCreatedOn(dates[0]);
			findTransactionError.setEndCreatedOn(dates[1]);
		}

		TransactionErrorSpecificationV2 spec = new TransactionErrorSpecificationV2(findTransactionError);
		List<TransactionError> results = transactionErrorRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

}
