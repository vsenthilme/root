package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.setup.model.docchecklist.DocCheckList;

@Repository
@Transactional
public interface DocCheckListRepository extends JpaRepository<DocCheckList,Long>, JpaSpecificationExecutor<DocCheckList> {
	
	public List<DocCheckList> findAll();
	
	public Optional<DocCheckList> 
	findByLanguageIdAndClassIdAndCheckListNoAndCaseCategoryIdAndCaseSubCategoryIdAndSequenceNoAndDeletionIndicator(
			String languageId, Long classId, Long checkListNo, Long caseCategoryId, Long caseSubCategoryId, Long sequenceNo, Long deletionIndicator);

	public Optional<DocCheckList> findByCheckListNoAndSequenceNoAndDeletionIndicator(Long checkListNo, Long sequenceNo,
			long l);

	public Optional<DocCheckList> findByCheckListNoAndDeletionIndicator(Long checkListNo, long l);
}