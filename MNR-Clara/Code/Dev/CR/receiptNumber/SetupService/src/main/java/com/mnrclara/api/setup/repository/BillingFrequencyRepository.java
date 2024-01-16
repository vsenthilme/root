package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.billingfrequency.BillingFrequency;

@Repository
public interface BillingFrequencyRepository extends JpaRepository<BillingFrequency, Long>{

	public List<BillingFrequency> findAll();
	Optional<BillingFrequency> findByBillingFrequencyId (Long billingFrequencyId);
	
	// `LANG_ID`, `CLASS_ID`, `BILL_FREQ_ID`, `IS_DELETED`
		Optional<BillingFrequency>
			findByLanguageIdAndClassIdAndBillingFrequencyIdAndDeletionIndicator 
			(String languageId, Long classId, Long billingFrequencyId, Long deletionIndicator);
}