package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.expensecode.ExpenseCode;

@Repository
public interface ExpenseCodeRepository extends JpaRepository<ExpenseCode, Long>{

	public List<ExpenseCode> findAll();
	Optional<ExpenseCode> findByExpenseCode (String expenseCodeId);
	
	// `LANG_ID`, `CLASS_ID`, `EXP_CODE`, `IS_DELETED`
	Optional<ExpenseCode>
		findByLanguageIdAndClassIdAndExpenseCodeAndDeletionIndicator 
		(String languageId, Long classId, String expenseCode, Long deletionIndicator);
}