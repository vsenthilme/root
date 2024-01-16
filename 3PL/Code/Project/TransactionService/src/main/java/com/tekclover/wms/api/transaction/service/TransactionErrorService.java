package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	 * createTransactionError
	 * @param newTransactionError
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
}
