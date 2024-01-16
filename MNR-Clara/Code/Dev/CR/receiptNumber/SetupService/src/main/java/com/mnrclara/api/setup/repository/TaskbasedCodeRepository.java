package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.taskbasedcode.TaskbasedCode;

@Repository
public interface TaskbasedCodeRepository extends JpaRepository<TaskbasedCode, Long>{

	public List<TaskbasedCode> findAll();
	Optional<TaskbasedCode> findByTaskCode (String taskbasedCodeId);
	
	// `LANG_ID`, `CLASS_ID`, `TASK_CODE`, `IS_DELETED`
	Optional<TaskbasedCode>
		findByLanguageIdAndClassIdAndTaskCodeAndDeletionIndicator 
		(String languageId, Long classId, String taskCode, Long deletionIndicator);
}