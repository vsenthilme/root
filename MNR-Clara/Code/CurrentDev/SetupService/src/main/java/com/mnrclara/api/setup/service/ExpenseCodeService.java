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
import com.mnrclara.api.setup.model.expensecode.AddExpenseCode;
import com.mnrclara.api.setup.model.expensecode.ExpenseCode;
import com.mnrclara.api.setup.model.expensecode.UpdateExpenseCode;
import com.mnrclara.api.setup.repository.ExpenseCodeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpenseCodeService {
	
	@Autowired
	private ExpenseCodeRepository expenseCodeRepository;
	
	public List<ExpenseCode> getExpenseCodes () {
		List<ExpenseCode> expenseCodeList = expenseCodeRepository.findAll();
		return expenseCodeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getExpenseCode
	 * @param expenseCodeId
	 * @return
	 */
	public ExpenseCode getExpenseCode (String expenseCodeId) {
		ExpenseCode expenseCode = expenseCodeRepository.findByExpenseCode(expenseCodeId).orElse(null);
		if (expenseCode.getDeletionIndicator() == 0) {
			return expenseCode;
		} else {
			throw new BadRequestException("The given ExpenseCode ID : " + expenseCodeId + " doesn't exist.");
		}
	}
	
	/**
	 * createExpenseCode
	 * @param newExpenseCode
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ExpenseCode createExpenseCode (AddExpenseCode newExpenseCode, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<ExpenseCode> expenseCode = 
				expenseCodeRepository.findByLanguageIdAndClassIdAndExpenseCodeAndDeletionIndicator(
						newExpenseCode.getLanguageId(),
						newExpenseCode.getClassId(),
						newExpenseCode.getExpenseCode(),
						0L);
		if (!expenseCode.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		ExpenseCode dbExpenseCode = new ExpenseCode();
		BeanUtils.copyProperties(newExpenseCode, dbExpenseCode, CommonUtils.getNullPropertyNames(newExpenseCode));
		
		log.info("dbExpenseCode ExpenseCode: " + dbExpenseCode.getExpenseCode());
		dbExpenseCode.setDeletionIndicator(0L);
		dbExpenseCode.setCreatedBy(loginUserID);
		dbExpenseCode.setUpdatedBy(loginUserID);
		dbExpenseCode.setCreatedOn(new Date());
		dbExpenseCode.setUpdatedOn(new Date());
		return expenseCodeRepository.save(dbExpenseCode);
	}
	
	/**
	 * updateExpenseCode
	 * @param expenseCodeId
	 * @param loginUserId 
	 * @param updateExpenseCode
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ExpenseCode updateExpenseCode (String expenseCodeId, String loginUserID, UpdateExpenseCode updateExpenseCode) 
			throws IllegalAccessException, InvocationTargetException {
		ExpenseCode dbExpenseCode = getExpenseCode(expenseCodeId);
		BeanUtils.copyProperties(updateExpenseCode, dbExpenseCode, CommonUtils.getNullPropertyNames(updateExpenseCode));
		dbExpenseCode.setUpdatedBy(loginUserID);
		dbExpenseCode.setUpdatedOn(new Date());
		return expenseCodeRepository.save(dbExpenseCode);
	}
	
	/**
	 * deleteExpenseCode
	 * @param loginUserID 
	 * @param expenseCodeCode
	 */
	public void deleteExpenseCode (String expenseCodeId, String loginUserID) {
		ExpenseCode expenseCode = getExpenseCode(expenseCodeId);
		if ( expenseCode != null) {
			expenseCode.setDeletionIndicator(1L);
			expenseCode.setUpdatedBy(loginUserID);
			expenseCodeRepository.save(expenseCode);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + expenseCodeId);
		}
	}
}
