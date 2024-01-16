package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.chartofaccounts.ChartOfAccounts;

@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Long>{

	public List<ChartOfAccounts> findAll();
	Optional<ChartOfAccounts> findByAccountNumber (String chartOfAccountsId);
	
	// `LANG_ID`, `CLASS_ID`, `ACCOUNT_NO`, `IS_DELETED`
	Optional<ChartOfAccounts>
		findByLanguageIdAndClassIdAndAccountNumberAndDeletionIndicator 
		(String languageId, Long classId, String accountNumber, Long deletionIndicator);
}