package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.transaction.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	public List<Transaction> findAll();
	public Transaction findByTransactionId(Long transactionId);
	
	// `LANG_ID`, `CLASS_ID`, `TRANS_ID`, `IS_DELETED`
	Optional<Transaction> findByLanguageIdAndClassIdAndTransactionIdAndDeletionIndicator
	(String languageId, Long classId, Long transactionId, Long deletionIndicator);
}