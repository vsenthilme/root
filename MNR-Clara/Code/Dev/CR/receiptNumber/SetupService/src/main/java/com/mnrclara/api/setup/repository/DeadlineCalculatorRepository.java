package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.deadlinecalculator.DeadlineCalculator;

@Repository
public interface DeadlineCalculatorRepository extends JpaRepository<DeadlineCalculator, Long> {

	public List<DeadlineCalculator> findAll();
	public Optional<DeadlineCalculator> findById(Long id);
	public Optional<DeadlineCalculator> findByDeadLineCalculationDays (Long deadlineCalculatorId);
	
	// `LANG_ID`, `CLASS_ID`, `TASK_TYP_CODE`, `IS_DELETED`
	Optional<DeadlineCalculator>
		findByLanguageIdAndClassIdAndTaskTypeCodeAndDeletionIndicator
		(String languageId, 
				Long classId, 
				String taskTypeCode, 
				Long deletionIndicator);
}