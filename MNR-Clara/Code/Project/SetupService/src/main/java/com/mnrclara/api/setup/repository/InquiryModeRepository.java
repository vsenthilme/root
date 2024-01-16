package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.inquirymode.InquiryMode;

@Repository
public interface InquiryModeRepository extends JpaRepository<InquiryMode, Long>{

	public List<InquiryMode> findAll();
	
	// `LANG_ID`, `CLASS_ID`, `INQ_MODE_ID`, `IS_DELETED`
	Optional<InquiryMode> 
		findByLanguageIdAndClassIdAndInquiryModeIdAndDeletionIndicator 
		(String languageId, Long classId, Long inquiryModeId, Long deletionIndicator);

	public InquiryMode findByInquiryModeId(Long inquiryModeId);
}