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
import com.mnrclara.api.setup.model.chartofaccounts.AddChartOfAccounts;
import com.mnrclara.api.setup.model.chartofaccounts.ChartOfAccounts;
import com.mnrclara.api.setup.model.chartofaccounts.UpdateChartOfAccounts;
import com.mnrclara.api.setup.repository.ChartOfAccountsRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChartOfAccountsService {
	
	@Autowired
	private ChartOfAccountsRepository chartOfAccountsRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<ChartOfAccounts> getChartOfAccountsList() {
		List<ChartOfAccounts> chartOfAccountsList =  chartOfAccountsRepository.findAll();
		return chartOfAccountsList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getChartOfAccounts
	 * @param chartOfAccountsId
	 * @return
	 */
	public ChartOfAccounts getChartOfAccounts (String accountNumber) {
		ChartOfAccounts chartOfAccounts = chartOfAccountsRepository.findByAccountNumber(accountNumber).orElse(null);
		if (chartOfAccounts.getDeletionIndicator() == 0) {
			return chartOfAccounts;
		} else {
			throw new BadRequestException("The given ChartOfAccounts ID : " + accountNumber + " doesn't exist.");
		}
	}
	
	/**
	 * createChartOfAccounts
	 * @param newChartOfAccounts
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ChartOfAccounts createChartOfAccounts (AddChartOfAccounts newChartOfAccounts, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<ChartOfAccounts> chartOfAccounts = 
				chartOfAccountsRepository.findByLanguageIdAndClassIdAndAccountNumberAndDeletionIndicator (
					newChartOfAccounts.getLanguageId(),
					newChartOfAccounts.getClassId(),
					newChartOfAccounts.getAccountNumber(),
					0L);
		if (!chartOfAccounts.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		ChartOfAccounts dbChartOfAccounts = new ChartOfAccounts();
		BeanUtils.copyProperties(newChartOfAccounts, dbChartOfAccounts);
		dbChartOfAccounts.setDeletionIndicator(0L);
		dbChartOfAccounts.setCreatedBy(loginUserID);
		dbChartOfAccounts.setUpdatedBy(loginUserID);
		dbChartOfAccounts.setCreatedOn(new Date());
		dbChartOfAccounts.setUpdatedOn(new Date());
		return chartOfAccountsRepository.save(dbChartOfAccounts);
	}
	
	/**
	 * updateChartOfAccounts
	 * @param chartOfAccountsId
	 * @param loginUserId 
	 * @param updateChartOfAccounts
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ChartOfAccounts updateChartOfAccounts (String chartOfAccountsId, String loginUserId, UpdateChartOfAccounts updateChartOfAccounts) 
			throws IllegalAccessException, InvocationTargetException {
		ChartOfAccounts dbChartOfAccounts = getChartOfAccounts(chartOfAccountsId);
		BeanUtils.copyProperties(updateChartOfAccounts, dbChartOfAccounts, CommonUtils.getNullPropertyNames(updateChartOfAccounts));
		dbChartOfAccounts.setUpdatedBy(loginUserId);
		dbChartOfAccounts.setUpdatedOn(new Date());
		return chartOfAccountsRepository.save(dbChartOfAccounts);
	}
	
	/**
	 * deleteChartOfAccounts
	 * @param accountNumber
	 * @param loginUserID 
	 */
	public void deleteChartOfAccounts (String accountNumber, String loginUserID) {
		ChartOfAccounts chartOfAccounts = getChartOfAccounts(accountNumber);
		if ( chartOfAccounts != null) {
			chartOfAccounts.setDeletionIndicator(1L);
			chartOfAccounts.setUpdatedBy(loginUserID);
			chartOfAccountsRepository.save(chartOfAccounts);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + accountNumber);
		}
	}
}
