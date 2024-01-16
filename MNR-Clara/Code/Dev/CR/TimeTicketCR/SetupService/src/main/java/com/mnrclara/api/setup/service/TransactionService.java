package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.transaction.AddTransaction;
import com.mnrclara.api.setup.model.transaction.Transaction;
import com.mnrclara.api.setup.model.transaction.UpdateTransaction;
import com.mnrclara.api.setup.repository.TransactionRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<Transaction> getTransactions () {
		List<Transaction> transactionList =  transactionRepository.findAll();
		transactionList = transactionList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return transactionList;
	}
	
	/**
	 * getTransaction
	 * @param transactionId
	 * @return
	 */
	public Transaction getTransaction (Long transactionId) {
		Transaction transaction = transactionRepository.findByTransactionId(transactionId);
		if (transaction != null && transaction.getDeletionIndicator() == 0) {
			return transaction;
		} else {
			throw new BadRequestException("The given Transaction ID : " + transactionId + " doesn't exist.");
		}
	}
	
	/**
	 * createTransaction
	 * @param newTransaction
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Transaction createTransaction (AddTransaction newTransaction, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<Transaction> transaction = 
				transactionRepository.findByLanguageIdAndClassIdAndTransactionIdAndDeletionIndicator (		
					newTransaction.getLanguageId(),
					newTransaction.getClassId(),
					newTransaction.getTransactionId(),
					0L);
		if (!transaction.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		Transaction dbTransaction = new Transaction();
		BeanUtils.copyProperties(newTransaction, dbTransaction);
		dbTransaction.setDeletionIndicator(0L);
		dbTransaction.setCreatedBy(loginUserID);
		dbTransaction.setUpdatedBy(loginUserID);
		dbTransaction.setCreatedOn(new Date());
		dbTransaction.setUpdatedOn(new Date());
		return transactionRepository.save(dbTransaction);
	}
	
	/**
	 * updateTransaction
	 * @param transactionId
	 * @param loginUserId 
	 * @param updateTransaction
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Transaction updateTransaction (Long transactionId, String loginUserID, UpdateTransaction updateTransaction) 
			throws IllegalAccessException, InvocationTargetException {
		Transaction dbTransaction = getTransaction(transactionId);
		BeanUtils.copyProperties(updateTransaction, dbTransaction, CommonUtils.getNullPropertyNames(updateTransaction));
		dbTransaction.setUpdatedBy(loginUserID);
		dbTransaction.setUpdatedOn(new Date());
		return transactionRepository.save(dbTransaction);
	}
	
	/**
	 * deleteTransaction
	 * @param loginUserID 
	 * @param transactionCode
	 */
	public void deleteTransaction (Long transactionModuleId, String loginUserID) {
		Transaction transaction = getTransaction(transactionModuleId);
		if ( transaction != null) {
			transaction.setDeletionIndicator(1L);
			transaction.setUpdatedBy(loginUserID);
			transactionRepository.save(transaction);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + transactionModuleId);
		}
	}
}
