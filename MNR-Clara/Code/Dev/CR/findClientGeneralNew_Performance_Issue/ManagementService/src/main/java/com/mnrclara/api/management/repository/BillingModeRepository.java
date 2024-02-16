package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.setup.BillingMode;

@Repository
public interface BillingModeRepository extends JpaRepository<BillingMode, Long>{

	public List<BillingMode> findAll();
	public BillingMode findByBillingModeId (Long billingModeId);
	
	// `LANG_ID`, `CLASS_ID`, `BILL_MODE_ID`, `IS_DELETED`
	public Optional<BillingMode>
		findByLanguageIdAndClassIdAndBillingModeIdAndDeletionIndicator 
		(String languageId, Long classId, String billingModeId, Long deletionIndicator);
}